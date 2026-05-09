package bg.tu_varna.sit.f24621674.parser;

import bg.tu_varna.sit.f24621674.exception.ParseException;
import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Rule;
import bg.tu_varna.sit.f24621674.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses файл с граматики в нашия текстов формат
 * Форматът на една граматика е:
 * GRAMMAR <name>
 * START <startNonTerminal>
 * <rule_1>
 * END
 *
 * Редове започващи с # са коментари и се игнорират
 */
public class GrammarFileParser {

    private final RuleParser ruleParser;

    /**
     * @param ruleParser четецът за правила който се използва вътрешно
     */
    public GrammarFileParser(RuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    /**
     * Прочита съдържанието на файл и връща всички намерени граматики
     * @param fileContent съдържанието на файла
     * @param idGenerator източник на уникални ID-та
     * @return списък с граматики
     */
    public List<Grammar> parseAll(String fileContent, IdGenerator idGenerator) {
        List<Grammar> grammars = new ArrayList<>();
        String[] lines = fileContent.split("\\r?\\n");

        Grammar current = null;
        boolean expectStart = false;

        for (int i = 0; i < lines.length; i++) {
            String raw = lines[i];
            String line = stripCommentAndTrim(raw);
            if (line.isEmpty()) {
                continue;
            }

            String upper = line.toUpperCase();

            if (upper.startsWith("GRAMMAR")) {
                if (current != null) {
                    throw new ParseException("Нова GRAMMAR започва преди END (ред " + (i + 1) + ")");
                }
                String name = line.substring("GRAMMAR".length()).trim();
                if (name.isEmpty()) {
                    name = "Grammar_" + (grammars.size() + 1);
                }
                // временно слагаме фиктивен стартов символ S ще го подменим при START
                current = new Grammar(idGenerator.nextId(), name, new NonTerminal("S"));
                expectStart = true;
            } else if (upper.startsWith("START")) {
                if (current == null) {
                    throw new ParseException("START извън GRAMMAR блок (ред " + (i + 1) + ")");
                }
                String start = line.substring("START".length()).trim();
                if (start.isEmpty()) {
                    throw new ParseException("Липсва ime на стартов нетерминал (ред " + (i + 1) + ")");
                }
                current.setStartSymbol(new NonTerminal(start));
                expectStart = false;
            } else if (upper.equals("END")) {
                if (current == null) {
                    throw new ParseException("END без съответен GRAMMAR (ред " + (i + 1) + ")");
                }
                grammars.add(current);
                current = null;
                expectStart = false;
            } else {
                if (current == null) {
                    throw new ParseException("Неочакван ред извън GRAMMAR блок (ред " + (i + 1) + "): " + line);
                }
                if (expectStart) {
                    // ако няма явен START приемаме че първото правило задава стартовия символ
                    Rule first = ruleParser.parse(line);
                    current.setStartSymbol(first.getLeft());
                    current.addRule(first);
                    expectStart = false;
                } else {
                    Rule r = ruleParser.parse(line);
                    current.addRule(r);
                }
            }
        }

        if (current != null) {
            throw new ParseException("Файлът завършва без END за граматика '" + current.getName() + "'");
        }
        return grammars;
    }

    /**
     * Премахва коментара от реда и изрязва празните символи
     */
    private String stripCommentAndTrim(String line) {
        int hash = line.indexOf('#');
        if (hash >= 0) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }

    /**
     * Записва една граматика в текстов формат
     * @param g граматиката за запис
     * @return текстовото представяне на граматиката
     */
    public String serialize(Grammar g) {
        StringBuilder sb = new StringBuilder();
        sb.append("GRAMMAR ").append(g.getName()).append('\n');
        sb.append("START ").append(g.getStartSymbol().getValue()).append('\n');
        for (Rule r : g.getRules()) {
            sb.append(r.toString()).append('\n');
        }
        sb.append("END").append('\n');
        return sb.toString();
    }

    /**
     * Записва няколко граматики в текстов формат с празен ред между тях
     * @param grammars списъкът граматики за запис
     * @return текстовото представяне на всички граматики
     */
    public String serializeAll(List<Grammar> grammars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grammars.size(); i++) {
            sb.append(serialize(grammars.get(i)));
            if (i + 1 < grammars.size()) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}