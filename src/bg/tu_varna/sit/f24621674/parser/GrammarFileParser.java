package bg.tu_varna.sit.f24621674.parser;


import bg.tu_varna.sit.f24621674.exception.ParseException;
import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Rule;
import java.util.ArrayList;
import java.util.List;
import bg.tu_varna.sit.f24621674.util.IdGenerator;


/**
 * Парсер за нашия собствен текстов формат за съхранение на граматики.
 *
 * <p><b>Формат на файл:</b> файлът може да съдържа една или повече граматики.
 * Една граматика изглежда така:</p>
 *
 * <pre>
 * GRAMMAR &lt;name&gt;
 * START &lt;startNonTerminal&gt;
 * &lt;rule_1&gt;
 * &lt;rule_2&gt;
 * ...
 * END
 * </pre>
 *
 * <p>Между две граматики могат да има произволни празни редове или коментари.
 * Коментарите започват с <code>#</code>. Празните редове се игнорират.</p>
 *
 * <p>За правилата вижте {@link RuleParser} – поддържат се и компактен, и
 * spaced синтаксис. В файла препоръчваме spaced (по-четим).</p>
 */
public class GrammarFileParser {

    private final RuleParser ruleParser;

    /**
     * @param ruleParser парсерът за правила, който да се ползва вътрешно
     */
    public GrammarFileParser(RuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    /**
     * Парсва съдържание на файл (като една дълга низова стойност) и връща
     * всички намерени граматики, всяка с уникално ID, генерирано от подадения
     * {@link IdGenerator}.
     *
     * @param fileContent цялото съдържание на файла
     * @param idGenerator източник на уникални ID-та
     * @return списък с граматики (може да е празен, ако файлът не съдържа такива)
     * @throws ParseException при структурна грешка
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
                    throw new ParseException("Нова GRAMMAR започва преди END (ред " + (i + 1) + ").");
                }
                String name = line.substring("GRAMMAR".length()).trim();
                if (name.isEmpty()) {
                    name = "Grammar_" + (grammars.size() + 1);
                }
                // временно слагаме фиктивен стартов символ S, ще го подменим при START
                current = new Grammar(idGenerator.nextId(), name, new NonTerminal("S"));
                expectStart = true;
            } else if (upper.startsWith("START")) {
                if (current == null) {
                    throw new ParseException("START извън GRAMMAR блок (ред " + (i + 1) + ").");
                }
                String start = line.substring("START".length()).trim();
                if (start.isEmpty()) {
                    throw new ParseException("Липсва име на стартов нетерминал (ред " + (i + 1) + ").");
                }
                current.setStartSymbol(new NonTerminal(start));
                expectStart = false;
            } else if (upper.equals("END")) {
                if (current == null) {
                    throw new ParseException("END без съответен GRAMMAR (ред " + (i + 1) + ").");
                }
                grammars.add(current);
                current = null;
                expectStart = false;
            } else {
                if (current == null) {
                    throw new ParseException("Неочакван ред извън GRAMMAR блок (ред " + (i + 1) + "): " + line);
                }
                if (expectStart) {
                    // ако няма явен START, приемаме че първото правило задава стартов символ
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
            throw new ParseException("Файлът завършва без END за граматика '" + current.getName() + "'.");
        }
        return grammars;
    }

    /**
     * Премахва коментара (от <code>#</code> нататък) и trim-ва реда.
     */
    private String stripCommentAndTrim(String line) {
        int hash = line.indexOf('#');
        if (hash >= 0) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }

    /**
     * Сериализира една граматика в текстов формат, готов за запис във файл.
     *
     * @param g граматиката за сериализация
     * @return многоредов низ
     */
    public String serialize(Grammar g) {
        StringBuilder sb = new StringBuilder();
        sb.append("GRAMMAR ").append(g.getName()).append('\n');
        StringBuilder start = sb.append("START ").append(g.getStartSymbol().getValue()).append('\n');
        for (Rule r : g.getRules()) {
            sb.append(r.toString()).append('\n');
        }
        sb.append("END").append('\n');
        return sb.toString();
    }

    /**
     * Сериализира няколко граматики – с празен ред между тях.
     *
     * @param grammars списъкът граматики
     * @return многоредов низ за запис
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
