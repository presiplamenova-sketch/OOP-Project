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
 * Parses текстово правило в обект Rule
 * Поддържа компактен синтаксис (S->aA) и разделен синтаксис (S -> a A)
 * Празна дясна страна ε eps или _ означават epsilon-продукция
 */
public class RuleParser {

    /** Разделителят между лява и дясна страна */
    private static final String ARROW = "->";

    /**
     * Parses един текстов ред в обект Rule
     * @param input низ с правило
     * @return новият Rule обект
     */
    public Rule parse(String input) {
        if (input == null) {
            throw new InvalidRuleException("Липсващ вход за правило");
        }
        String trimmed = input.trim();
        int arrowIdx = trimmed.indexOf(ARROW);
        if (arrowIdx < 0) {
            throw new InvalidRuleException("Липсва оператор '->' в '" + input + "'");
        }
        String leftStr = trimmed.substring(0, arrowIdx).trim();
        String rightStr = trimmed.substring(arrowIdx + ARROW.length()).trim();

        NonTerminal left = parseLeft(leftStr);
        List<Symbol> right = parseRight(rightStr);
        return new Rule(left, right);
    }

    /**
     * Parses лявата страна - точно един нетерминал
     * @param leftStr низ с името на нетерминала
     * @return съответният NonTerminal обект
     */
    private NonTerminal parseLeft(String leftStr) {
        if (leftStr.isEmpty()) {
            throw new InvalidRuleException("Лявата страна е празна");
        }
        if (!SymbolUtils.isNonTerminalToken(leftStr)) {
            throw new InvalidRuleException(
                    "Лявата страна '" + leftStr + "' не е валиден нетерминал");
        }
        return new NonTerminal(leftStr);
    }

    /**
     * Parses дясната страна и връща списък от символи
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
     * Parses дясна страна в която символите са разделени с интервали
     */
    private List<Symbol> parseSpaced(String rightStr) {
        List<Symbol> result = new ArrayList<>();
        String[] tokens = rightStr.split("\\s+");
        for (String token : Arrays.stream(tokens).filter(s -> !s.isEmpty()).toList()) {
            if (SymbolUtils.isEpsilonToken(token)) {
                continue;
            }
            if (SymbolUtils.isNonTerminalToken(token)) {
                result.add(new NonTerminal(token));
            } else if (SymbolUtils.isTerminalToken(token)) {
                result.add(new Terminal(token));
            } else {
                throw new InvalidRuleException(
                        "Токенът '" + token + "' не е нито терминал нито нетерминал");
            }
        }
        return result;
    }

    /**
     * Parses дясна страна в компактен формат - всеки символ е един знак
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
                continue;
            } else {
                throw new InvalidRuleException(
                        "Неразпознат символ '" + c + "' в '" + rightStr + "'");
            }
        }
        return result;
    }
}