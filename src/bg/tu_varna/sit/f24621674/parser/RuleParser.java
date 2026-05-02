package bg.tu_varna.sit.f24621674.parser;


import bg.tu_varna.sit.f24621674.exception.InvalidRuleException;
import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Rule;
import bg.tu_varna.sit.f24621674.model.Symbol;
import bg.tu_varna.sit.f24621674.model.Terminal;
import bg.tu_varna.sit.f24621674.util.SymbolUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Парсер за правило от текстов вид в обект {@link Rule}.
 *
 * <p>Поддържа два стила на писане:</p>
 * <ul>
 *   <li><b>Компактен</b> (без интервали отдясно): <code>S-&gt;aA</code> – всеки
 *       символ е отделна буква/цифра.</li>
 *   <li><b>С интервали</b>: <code>S -&gt; a A X1</code> – символите са разделени,
 *       позволени са многосимволни нетерминали (напр. генерирани при CNF).</li>
 * </ul>
 *
 * <p>Празна дясна страна, <code>ε</code>, <code>eps</code> или <code>_</code>
 * означават epsilon-продукция.</p>
 */
public class RuleParser {

    /** Разделителят между ляво и дясно. */
    private static final String ARROW = "->";

    /**
     * Парсва един текстов ред до {@link Rule}.
     *
     * @param input низ с правило
     * @return нов {@link Rule}
     * @throws InvalidRuleException при всякакви синтактични грешки
     */
    public Rule parse(String input) {
        if (input == null) {
            throw new InvalidRuleException("Липсващ вход за правило.");
        }
        String trimmed = input.trim();
        int arrowIdx = trimmed.indexOf(ARROW);
        if (arrowIdx < 0) {
            throw new InvalidRuleException("Липсва оператор '->' в '" + input + "'.");
        }
        String leftStr = trimmed.substring(0, arrowIdx).trim();
        String rightStr = trimmed.substring(arrowIdx + ARROW.length()).trim();

        NonTerminal left = parseLeft(leftStr);
        List<Symbol> right = parseRight(rightStr);
        return new Rule(left, right);
    }

    /**
     * Парсва лявата страна – точно един нетерминал.
     * @param leftStr низ, който би трябвало да е име на нетерминал
     * @return съответният {@link NonTerminal}
     */
    private NonTerminal parseLeft(String leftStr) {
        if (leftStr.isEmpty()) {
            throw new InvalidRuleException("Лявата страна е празна.");
        }
        // може да е едно- или многосимволно име, но трябва да е валиден нетерминал
        if (!SymbolUtils.isNonTerminalToken(leftStr)) {
            throw new InvalidRuleException(
                    "Лявата страна '" + leftStr + "' не е валиден нетерминал.");
        }
        return new NonTerminal(leftStr);
    }

    /**
     * Парсва дясната страна – връща списък от символи (евентуално празен за epsilon).
     * @param rightStr низ на дясната страна
     * @return списък от символи
     */
    private List<Symbol> parseRight(String rightStr) {
        if (SymbolUtils.isEpsilonToken(rightStr)) {
            return List.of();
        }
        if (rightStr.contains(" ")) {
            return parseSpaced(rightStr);
        }
        return parseCompact(rightStr);
    }

    /**
     * Парсира дясна страна, в която символите са разделени с интервали
     * (позволяваме многосимволни имена).
     */
    private List<Symbol> parseSpaced(String rightStr) {
        List<Symbol> result = new ArrayList<>();
        String[] tokens = rightStr.split("\\s+");
        for (String token : Arrays.stream(tokens).filter(s -> !s.isEmpty()).toList()) {
            if (SymbolUtils.isEpsilonToken(token)) {
                // явен epsilon токен в средата е странно, но не го броим като символ
                continue;
            }
            if (SymbolUtils.isNonTerminalToken(token)) {
                result.add(new NonTerminal(token));
            } else if (SymbolUtils.isTerminalToken(token)) {
                result.add(new Terminal(token));
            } else {
                throw new InvalidRuleException(
                        "Токенът '" + token + "' не е нито терминал, нито нетерминал.");
            }
        }
        return result;
    }

    /**
     * Парсира дясна страна в компактен формат – всеки символ е един знак
     * (главна буква => нетерминал, малка/цифра => терминал).
     */
    private List<Symbol> parseCompact(String rightStr) {
        List<Symbol> result = new ArrayList<>();
        for (int i = 0; i < rightStr.length(); i++) {
            char c = rightStr.charAt(i);
            if (SymbolUtils.isNonTerminalChar(c)) {
                result.add(new NonTerminal(String.valueOf(c)));
            } else if (SymbolUtils.isTerminalChar(c)) {
                result.add(new Terminal(String.valueOf(c)));
            } else if (Character.isWhitespace(c)) {
                // не очакваме, но сме толерантни
                continue;
            } else {
                throw new InvalidRuleException(
                        "Неразпознат символ '" + c + "' в '" + rightStr + "'.");
            }
        }
        return result;
    }
}
