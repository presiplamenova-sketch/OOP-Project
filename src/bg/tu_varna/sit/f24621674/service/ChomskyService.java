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
 * Сервиз за работа с нормална форма на Чомски
 * Граматиката е в НФЧ ако всяко правило е от вид A->BC или A->a
 * Преобразуването минава през стъпки START DEL UNIT TERM BIN
 */
public class ChomskyService {

    private final IdGenerator idGenerator;

    /**
     * @param idGenerator източник на ID-та за новата граматика
     */
    public ChomskyService(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Проверява дали граматиката е в нормална форма на Чомски
     * @param g граматиката за проверка
     * @return true ако е в НФЧ
     */
    public boolean isInChomskyNormalForm(Grammar g) {
        NonTerminal start = g.getStartSymbol();
        boolean hasStartEpsilon = g.getRules().stream()
                .anyMatch(r -> r.getLeft().equals(start) && r.isEpsilon());

        if (hasStartEpsilon) {
            // стартовият символ не трябва да се среща в дясна страна
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
                // epsilon е позволено само за стартовия символ
                if (!r.getLeft().equals(start)) {
                    return false;
                }
            } else if (rhs.size() == 1) {
                // трябва да е терминал
                if (!rhs.get(0).isTerminal()) {
                    return false;
                }
            } else if (rhs.size() == 2) {
                // двата символа трябва да са нетерминали
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
     * Преобразува граматика до НФЧ и връща нова граматика
     * Оригиналната граматика не се променя
     * @param source изходната граматика
     * @return нова граматика в НФЧ
     */
    public Grammar toChomskyNormalForm(Grammar source) {
        List<Rule> rules = new ArrayList<>();
        for (Rule r : source.getRules()) {
            rules.add(new Rule(r.getLeft(), new ArrayList<>(r.getRight())));
        }
        NonTerminal start = source.getStartSymbol();
        Set<String> usedNames = collectUsedNames(rules, start);

        // 1) START - добавяме нов стартов символ ако е нужно
        if (startAppearsOnRight(rules, start)) {
            NonTerminal newStart = fresh(usedNames, "S0");
            rules.add(0, new Rule(newStart, List.of(start)));
            start = newStart;
        }

        // 2 - DEL - премахваме epsilon продукциите
        rules = eliminateEpsilonRules(rules, start);

        // 3 - UNIT - премахваме единичните продукции
        rules = eliminateUnitRules(rules);

        // 4 - TERM - заменяме терминалите в дълги правила
        rules = replaceTerminalsInLongRules(rules, usedNames);

        // 5- BIN - разбиваме дългите правила на бинарни
        rules = binarize(rules, usedNames);

        rules = dedup(rules);

        Grammar result = new Grammar(idGenerator.nextId(),
                "CNF(" + source.getName() + ")", start);
        for (Rule r : rules) {
            result.addRule(r);
        }
        return result;
    }

    /**
     * Събира имената на всички нетерминали в граматиката
     */
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

    /**
     * Проверява дали стартовият символ се среща в дясна страна на правило
     */
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

    /**
     * Генерира нов нетерминал чието име не се среща в използваните имена
     */
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
     * Намира всички nullable нетерминали за които A =>* epsilon
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
     * Премахва epsilon продукциите и генерира всички комбинации без nullable символи
     * Запазва S->epsilon ако стартовият символ е nullable
     */
    private List<Rule> eliminateEpsilonRules(List<Rule> rules, NonTerminal start) {
        Set<NonTerminal> nullable = findNullable(rules);
        List<Rule> out = new ArrayList<>();
        Set<Rule> seen = new LinkedHashSet<>();

        for (Rule r : rules) {
            if (r.isEpsilon()) {
                continue;
            }
            List<List<Symbol>> expansions = expandNullable(r.getRight(), nullable);
            for (List<Symbol> rhs : expansions) {
                if (rhs.isEmpty()) {
                    continue;
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
     * Генерира всички комбинации на дясна страна с и без nullable символи
     */
    private List<List<Symbol>> expandNullable(List<Symbol> rhs, Set<NonTerminal> nullable) {
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
                } else {
                    newRhs.add(rhs.get(i));
                }
            }
            out.add(newRhs);
        }
        return out;
    }

    /**
     * Премахва единичните продукции A->B като прехвърля правилата на B към A
     */
    private List<Rule> eliminateUnitRules(List<Rule> rules) {
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
     * Заменя терминалите в дълги правила с нови нетерминали
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
     * Разбива правила с повече от два символа на бинарни правила
     * Пример: A->BCDE става A->BX1 X1->CX2 X2->DE
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
            NonTerminal x = fresh(used, "X");
            out.add(new Rule(left, Arrays.asList(rhs.get(0), x)));
            for (int i = 1; i < rhs.size() - 2; i++) {
                NonTerminal nextX = fresh(used, "X");
                out.add(new Rule(x, Arrays.asList(rhs.get(i), nextX)));
                x = nextX;
            }
            out.add(new Rule(x, Arrays.asList(rhs.get(rhs.size() - 2), rhs.get(rhs.size() - 1))));
        }
        return out;
    }

    /**
     * Премахва дублиращи се правила
     */
    private List<Rule> dedup(List<Rule> rules) {
        Set<Rule> seen = new LinkedHashSet<>(rules);
        return new ArrayList<>(seen);
    }
}