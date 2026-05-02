package bg.tu_varna.sit.f24621674.service;

import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Rule;
import bg.tu_varna.sit.f24621674.model.Symbol;
import bg.tu_varna.sit.f24621674.model.Terminal;
import bg.tu_varna.sit.f24621674.util.IdGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Сервиз за работа с нормална форма на Чомски (НФЧ / CNF).
 *
 * <p>Граматиката е в НФЧ, ако всяко правило е в един от следните видове:</p>
 * <ul>
 *   <li><code>A → BC</code> (точно два нетерминала отдясно);</li>
 *   <li><code>A → a</code> (точно един терминал отдясно);</li>
 *   <li><code>S → ε</code> (само за стартовия символ, ако ε ∈ L).
 *       При това стартовият символ не трябва да се появява в нито една дясна
 *       страна – обикновено се въвежда нов стартов символ S0.</li>
 * </ul>
 *
 * <p>За преобразуването се използва класически подход, разделен на стъпки:</p>
 * <ol>
 *   <li>START – вкарва се нов стартов символ S0 → S, ако S се среща в дясна страна.</li>
 *   <li>DEL – премахват се ε-продукциите (с изключение на S0 → ε, ако е необходимо).</li>
 *   <li>UNIT – премахват се единичните продукции A → B.</li>
 *   <li>TERM – в правила с дясна страна с дължина ≥ 2 всеки терминал се заменя
 *       с нов нетерминал T_a и се добавя T_a → a.</li>
 *   <li>BIN – правила с дясна страна дължина &gt; 2 се разбиват на бинарни.</li>
 * </ol>
 *
 * <p>Забележка: това е реална имплементация, но за много големи граматики
 * резултатите могат да растат експоненциално (най-вече при DEL стъпката). За
 * учебни/демонстрационни размери работи коректно.</p>
 */
public class ChomskyService {

    private final IdGenerator idGenerator;

    /**
     * @param idGenerator източник на ID-та за новата CNF граматика
     */
    public ChomskyService(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Проверява дали подадената граматика е в нормална форма на Чомски.
     *
     * @param g граматика
     * @return true ако е в НФЧ
     */
    public boolean isInChomskyNormalForm(Grammar g) {
        NonTerminal start = g.getStartSymbol();
        // (По желание) проверяваме, че стартът не се среща в дясна страна при S→ε
        boolean hasStartEpsilon = g.getRules().stream()
                .anyMatch(r -> r.getLeft().equals(start) && r.isEpsilon());

        if (hasStartEpsilon) {
            // за строга НФЧ стартът не бива да е в дясна страна
            for (Rule r : g.getRules()) {
                for (Symbol s : r.getRight()) {
                    if (s.equals(start)) {
                        return false;
                    }
                }
            }
        }

        for (Rule r : g.getRules()) {
            List<Symbol> rhs = r.getRight();
            if (rhs.isEmpty()) {
                // позволено само за стартовия
                if (!r.getLeft().equals(start)) {
                    return false;
                }
            } else if (rhs.size() == 1) {
                // трябва да е терминал
                if (!rhs.get(0).isTerminal()) {
                    return false;
                }
            } else if (rhs.size() == 2) {
                // двата са нетерминали
                if (!rhs.get(0).isNonTerminal() || !rhs.get(1).isNonTerminal()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Преобразува граматика до НФЧ. Връща нова граматика – оригиналната
     * не се мутира.
     *
     * @param source изходна граматика
     * @return нова граматика в НФЧ
     */
    public Grammar toChomskyNormalForm(Grammar source) {
        // работим с mutable списък правила – ще правим много стъпки
        List<Rule> rules = new ArrayList<>();
        for (Rule r : source.getRules()) {
            rules.add(new Rule(r.getLeft(), new ArrayList<>(r.getRight())));
        }
        NonTerminal start = source.getStartSymbol();
        Set<String> usedNames = collectUsedNames(rules, start);

        // --- 1) START: ако старт се среща в дясна страна, добавяме нов старт ---
        if (startAppearsOnRight(rules, start)) {
            NonTerminal newStart = fresh(usedNames, "S0");
            rules.add(0, new Rule(newStart, List.of(start)));
            start = newStart;
        }

        // --- 2) DEL: премахване на ε-продукциите ---
        rules = eliminateEpsilonRules(rules, start);

        // --- 3) UNIT: премахване на единичните продукции ---
        rules = eliminateUnitRules(rules);

        // --- 4) TERM: в дълги правила заменяме терминалите с нови нетерминали ---
        rules = replaceTerminalsInLongRules(rules, usedNames);

        // --- 5) BIN: разбиваме дълги правила на бинарни ---
        rules = binarize(rules, usedNames);

        // премахваме дубликати, запазвайки ред
        rules = dedup(rules);

        // строим нова граматика
        Grammar result = new Grammar(idGenerator.nextId(),
                "CNF(" + source.getName() + ")", start);
        for (Rule r : rules) {
            result.addRule(r);
        }
        return result;
    }


    private Set<String> collectUsedNames(List<Rule> rules, NonTerminal start) {
        Set<String> used = new LinkedHashSet<>();
        used.add(start.getValue());
        for (Rule r : rules) {
            used.add(r.getLeft().getValue());
            for (Symbol s : r.getRight()) {
                if (s.isNonTerminal()) {
                    used.add(s.getValue());
                }
            }
        }
        return used;
    }

    private boolean startAppearsOnRight(List<Rule> rules, NonTerminal start) {
        for (Rule r : rules) {
            for (Symbol s : r.getRight()) {
                if (s.equals(start)) {
                    return true;
                }
            }
        }
        return false;
    }

    private NonTerminal fresh(Set<String> used, String base) {
        if (!used.contains(base)) {
            used.add(base);
            return new NonTerminal(base);
        }
        int i = 1;
        while (true) {
            String candidate = base + i;
            if (!used.contains(candidate)) {
                used.add(candidate);
                return new NonTerminal(candidate);
            }
            i++;
        }
    }

    /**
     * Намира всички "nullable" нетерминали – такива, за които A ⇒* ε.
     */
    private Set<NonTerminal> findNullable(List<Rule> rules) {
        Set<NonTerminal> nullable = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule r : rules) {
                if (nullable.contains(r.getLeft())) {
                    continue;
                }
                if (r.isEpsilon()) {
                    nullable.add(r.getLeft());
                    changed = true;
                } else {
                    boolean allNullable = r.getRight().stream()
                            .allMatch(s -> s.isNonTerminal() && nullable.contains((NonTerminal) s));
                    if (allNullable) {
                        nullable.add(r.getLeft());
                        changed = true;
                    }
                }
            }
        }
        return nullable;
    }

    /**
     * Премахва ε-продукциите. За всяко правило A → α генерира всички
     * комбинации, в които nullable символи присъстват или липсват.
     * Запазва S → ε ако стартовият символ е nullable.
     */
    private List<Rule> eliminateEpsilonRules(List<Rule> rules, NonTerminal start) {
        Set<NonTerminal> nullable = findNullable(rules);
        List<Rule> out = new ArrayList<>();
        Set<Rule> seen = new LinkedHashSet<>();

        for (Rule r : rules) {
            if (r.isEpsilon()) {
                // запазваме само S → ε, ако стартът е nullable
                continue;
            }
            List<List<Symbol>> expansions = expandNullable(r.getRight(), nullable);
            for (List<Symbol> rhs : expansions) {
                if (rhs.isEmpty()) {
                    continue; // ε продукциите ги пускаме отделно
                }
                Rule nr = new Rule(r.getLeft(), rhs);
                if (seen.add(nr)) {
                    out.add(nr);
                }
            }
        }
        if (nullable.contains(start)) {
            Rule startEps = new Rule(start, List.of());
            if (seen.add(startEps)) {
                out.add(startEps);
            }
        }
        return out;
    }

    /**
     * Генерира всички комбинации на дясна страна, в които можем да изтрием
     * подмножество от позициите, заемани от nullable нетерминали.
     */
    private List<List<Symbol>> expandNullable(List<Symbol> rhs, Set<NonTerminal> nullable) {
        // събираме индексите на nullable нетерминалите
        List<Integer> nullableIdx = new ArrayList<>();
        for (int i = 0; i < rhs.size(); i++) {
            Symbol s = rhs.get(i);
            if (s.isNonTerminal() && nullable.contains((NonTerminal) s)) {
                nullableIdx.add(i);
            }
        }
        int k = nullableIdx.size();
        int subsets = 1 << k;
        List<List<Symbol>> out = new ArrayList<>();
        for (int mask = 0; mask < subsets; mask++) {
            // mask битовете = "оставяме", 0 = "махаме"
            Set<Integer> keep = new HashSet<>();
            for (int b = 0; b < k; b++) {
                if ((mask & (1 << b)) != 0) {
                    keep.add(nullableIdx.get(b));
                }
            }
            List<Symbol> newRhs = new ArrayList<>();
            for (int i = 0; i < rhs.size(); i++) {
                if (nullableIdx.contains(i)) {
                    if (keep.contains(i)) {
                        newRhs.add(rhs.get(i));
                    }
                    // иначе пропускаме
                } else {
                    newRhs.add(rhs.get(i));
                }
            }
            out.add(newRhs);
        }
        return out;
    }

    /**
     * Премахва единични продукции A → B. За всяка двойка (A, B), за която
     * A ⇒* B само чрез единични правила, прехвърля неединичните правила на B към A.
     */
    private List<Rule> eliminateUnitRules(List<Rule> rules) {
        // 1) намираме за всеки A множеството unit-reachable нетерминали
        Map<NonTerminal, Set<NonTerminal>> unitReach = new HashMap<>();
        Set<NonTerminal> allNT = new LinkedHashSet<>();
        for (Rule r : rules) {
            allNT.add(r.getLeft());
        }
        for (NonTerminal nt : allNT) {
            Set<NonTerminal> reach = new LinkedHashSet<>();
            reach.add(nt);
            boolean changed = true;
            while (changed) {
                changed = false;
                for (NonTerminal a : new ArrayList<>(reach)) {
                    for (Rule r : rules) {
                        if (!r.getLeft().equals(a)) {
                            continue;
                        }
                        if (r.getRight().size() == 1 && r.getRight().get(0).isNonTerminal()) {
                            NonTerminal b = (NonTerminal) r.getRight().get(0);
                            if (reach.add(b)) {
                                changed = true;
                            }
                        }
                    }
                }
            }
            unitReach.put(nt, reach);
        }

        List<Rule> out = new ArrayList<>();
        Set<Rule> seen = new LinkedHashSet<>();
        for (NonTerminal a : allNT) {
            for (NonTerminal b : unitReach.get(a)) {
                for (Rule r : rules) {
                    if (!r.getLeft().equals(b)) {
                        continue;
                    }
                    // прескачаме единичните – те са "unit", не копираме
                    if (r.getRight().size() == 1 && r.getRight().get(0).isNonTerminal()) {
                        continue;
                    }
                    Rule nr = new Rule(a, r.getRight());
                    if (seen.add(nr)) {
                        out.add(nr);
                    }
                }
            }
        }
        return out;
    }

    /**
     * В правила с дясна страна с дължина ≥ 2, заменя всеки терминал с нов
     * нетерминал T_x и добавя правило T_x → x (ако още не е добавено).
     */
    private List<Rule> replaceTerminalsInLongRules(List<Rule> rules, Set<String> used) {
        Map<Terminal, NonTerminal> termToNT = new HashMap<>();
        List<Rule> out = new ArrayList<>();

        for (Rule r : rules) {
            if (r.getRight().size() < 2) {
                out.add(r);
                continue;
            }
            List<Symbol> newRhs = new ArrayList<>();
            for (Symbol s : r.getRight()) {
                if (s.isTerminal()) {
                    Terminal t = (Terminal) s;
                    NonTerminal nt = termToNT.get(t);
                    if (nt == null) {
                        nt = fresh(used, "T" + t.getValue().toUpperCase());
                        termToNT.put(t, nt);
                        out.add(new Rule(nt, List.of(t)));
                    }
                    newRhs.add(nt);
                } else {
                    newRhs.add(s);
                }
            }
            out.add(new Rule(r.getLeft(), newRhs));
        }
        return out;
    }

    /**
     * Превръща правила с дясна страна > 2 в поредица от бинарни правила.
     * Пример: A → B C D E става A → B X1, X1 → C X2, X2 → D E.
     */
    private List<Rule> binarize(List<Rule> rules, Set<String> used) {
        List<Rule> out = new ArrayList<>();
        for (Rule r : rules) {
            if (r.getRight().size() <= 2) {
                out.add(r);
                continue;
            }
            List<Symbol> rhs = r.getRight();
            NonTerminal left = r.getLeft();
            // A → s0 X1
            NonTerminal x = fresh(used, "X");
            out.add(new Rule(left, Arrays.asList(rhs.get(0), x)));
            // средните
            for (int i = 1; i < rhs.size() - 2; i++) {
                NonTerminal nextX = fresh(used, "X");
                out.add(new Rule(x, Arrays.asList(rhs.get(i), nextX)));
                x = nextX;
            }
            // последното: Xk → s_{n-2} s_{n-1}
            out.add(new Rule(x, Arrays.asList(rhs.get(rhs.size() - 2), rhs.get(rhs.size() - 1))));
        }
        return out;
    }

    private List<Rule> dedup(List<Rule> rules) {
        Set<Rule> seen = new LinkedHashSet<>(rules);
        return new ArrayList<>(seen);
    }
}

