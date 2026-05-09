package bg.tu_varna.sit.f24621674.service;

import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.Rule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервиз за форматиране и визуализация на граматики
 * Съдържа методи за показване на граматика в конзолата
 */
public class GrammarService {

    /**
     * Форматира граматика в четим вид с номерирани правила
     * @param grammar граматиката за показване
     * @return форматиран низ за конзолата
     */
    public String formatForPrint(Grammar grammar) {
        StringBuilder sb = new StringBuilder();
        sb.append("Граматика #").append(grammar.getId())
                .append(" (").append(grammar.getName()).append(")").append('\n');
        sb.append("  Стартов символ: ").append(grammar.getStartSymbol().getValue()).append('\n');
        sb.append("  Нетерминали: ").append(
                grammar.getNonTerminals().stream().map(nt -> nt.getValue()).collect(Collectors.joining(", "))
        ).append('\n');
        sb.append("  Терминали:   ").append(
                grammar.getTerminals().stream().map(t -> t.getValue()).collect(Collectors.joining(", "))
        ).append('\n');
        sb.append("  Правила:").append('\n');
        List<Rule> rules = grammar.getRules();
        if (rules.isEmpty()) {
            sb.append("    (няма)").append('\n');
        } else {
            for (int i = 0; i < rules.size(); i++) {
                sb.append(String.format("    %d) %s%n", i + 1, rules.get(i).toString()));
            }
        }
        return sb.toString();
    }

    /**
     * Форматира граматика в кратък еднорядков вид за командата list
     * @param grammar граматиката за показване
     * @return кратко описание от вида #1 MyGrammar (start: S, 5 правила)
     */
    public String formatForList(Grammar grammar) {
        return String.format("#%d  %s  (start: %s, %d правила)",
                grammar.getId(),
                grammar.getName(),
                grammar.getStartSymbol().getValue(),
                grammar.getRules().size());
    }
}