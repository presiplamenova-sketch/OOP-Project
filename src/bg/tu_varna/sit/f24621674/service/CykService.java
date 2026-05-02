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
 * Имплементация на алгоритъма CYK (Cocke-Younger-Kasami).
 *
 * <p>CYK приема граматика в НФЧ и дума w; отговаря на въпроса дали
 * <code>w ∈ L(G)</code>. Ако подадената граматика не е в НФЧ, този сервиз
 * я преобразува с помощта на {@link ChomskyService} и работи върху новата.</p>
 *
 * <p>Алгоритъмът попълва матрица <code>T[i][j]</code> от множества от
 * нетерминали, където <code>T[i][j]</code> = множеството от нетерминали,
 * които извеждат подниза <code>w[i..j]</code> (индексирани от 0, включително j).</p>
 *
 * <p>Сложност: O(n³ · |G|) по време, O(n²) по памет, където n = |w|.</p>
 */
public class CykService {

    private final ChomskyService chomskyService;

    /**
     * @param chomskyService използва се за конверсия до НФЧ при нужда
     */
    public CykService(ChomskyService chomskyService) {
        this.chomskyService = chomskyService;
    }

    /**
     * Проверява дали думата принадлежи на езика на граматиката.
     *
     * @param grammar граматика (не е задължително в НФЧ)
     * @param word дума, която искаме да проверим (низ от терминали)
     * @return true ако <code>word ∈ L(grammar)</code>
     */
    public boolean belongs(Grammar grammar, String word) {
        Grammar cnf = chomskyService.isInChomskyNormalForm(grammar)
                ? grammar
                : chomskyService.toChomskyNormalForm(grammar);

        if (word == null) {
            word = "";
        }

        // Специален случай: празна дума
        if (word.isEmpty()) {
            return hasStartEpsilon(cnf);
        }

        int n = word.length();
        // T[i][j] – множество от нетерминали, извеждащи word[i..j]
        @SuppressWarnings("unchecked")
        Set<NonTerminal>[][] table = new HashSet[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                table[i][j] = new HashSet<>();
            }
        }

        // базова стъпка – единични символи
        for (int i = 0; i < n; i++) {
            Terminal t = new Terminal(String.valueOf(word.charAt(i)));
            for (Rule r : cnf.getRules()) {
                if (r.getRight().size() == 1 && r.getRight().get(0).equals(t)) {
                    table[i][i].add(r.getLeft());
                }
            }
        }

        // индуктивна стъпка – дължини от 2 до n
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
     * @return true ако стартовият символ има директна ε-продукция
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
