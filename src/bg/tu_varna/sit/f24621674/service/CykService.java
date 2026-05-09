package bg.tu_varna.sit.f24621674.service;

import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Rule;
import bg.tu_varna.sit.f24621674.model.Symbol;
import bg.tu_varna.sit.f24621674.model.Terminal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сервиз за алгоритъма CYK (Cocke-Younger-Kasami)
 * Проверява дали дума принадлежи на езика на граматика
 * Ако граматиката не е в НФЧ тя се преобразува автоматично
 */
public class CykService {

    private final ChomskyService chomskyService;

    /**
     * @param chomskyService използва се за преобразуване до НФЧ при нужда
     */
    public CykService(ChomskyService chomskyService) {
        this.chomskyService = chomskyService;
    }

    /**
     * Проверява дали думата принадлежи на езика на граматиката
     * @param grammar граматиката за проверка
     * @param word думата която искаме да проверим
     * @return true ако думата принадлежи на езика
     */
    public boolean belongs(Grammar grammar, String word) {
        Grammar cnf = chomskyService.isInChomskyNormalForm(grammar)
                ? grammar
                : chomskyService.toChomskyNormalForm(grammar);

        if (word == null) {
            word = "";
        }

        // проверка за празна дума
        if (word.isEmpty()) {
            return hasStartEpsilon(cnf);
        }

        int n = word.length();
        // T[i][j] съдържа нетерминалите които извеждат word[i..j]
        @SuppressWarnings("unchecked")
        Set<NonTerminal>[][] table = new HashSet[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                table[i][j] = new HashSet<>();
            }
        }

        // базова стъпка - единични символи
        for (int i = 0; i < n; i++) {
            Terminal t = new Terminal(String.valueOf(word.charAt(i)));
            for (Rule r : cnf.getRules()) {
                if (r.getRight().size() == 1 && r.getRight().get(0).equals(t)) {
                    table[i][i].add(r.getLeft());
                }
            }
        }

        // индуктивна стъпка - дължини от 2 до n
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i <= n - length; i++) {
                int j = i + length - 1;
                for (int k = i; k < j; k++) {
                    Set<NonTerminal> left = table[i][k];
                    Set<NonTerminal> right = table[k + 1][j];
                    if (left.isEmpty() || right.isEmpty()) {
                        continue;
                    }
                    for (Rule r : cnf.getRules()) {
                        List<Symbol> rhs = r.getRight();
                        if (rhs.size() != 2) {
                            continue;
                        }
                        if (rhs.get(0).isNonTerminal() && rhs.get(1).isNonTerminal()
                                && left.contains((NonTerminal) rhs.get(0))
                                && right.contains((NonTerminal) rhs.get(1))) {
                            table[i][j].add(r.getLeft());
                        }
                    }
                }
            }
        }

        return table[0][n - 1].contains(cnf.getStartSymbol());
    }

    /**
     * Проверява дали стартовият символ има epsilon продукция
     */
    private boolean hasStartEpsilon(Grammar cnf) {
        NonTerminal start = cnf.getStartSymbol();
        for (Rule r : cnf.getRules()) {
            if (r.getLeft().equals(start) && r.isEpsilon()) {
                return true;
            }
        }
        return false;
    }
}