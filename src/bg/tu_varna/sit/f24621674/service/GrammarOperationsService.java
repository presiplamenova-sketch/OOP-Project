package bg.tu_varna.sit.f24621674.service;

import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Rule;
import bg.tu_varna.sit.f24621674.model.Symbol;
import bg.tu_varna.sit.f24621674.util.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Сервиз за операции над граматики
 * Поддържа обединение конкатенация итерация и проверка за празен език
 * Операциите не променят оригиналните граматики а връщат нова
 */
public class GrammarOperationsService {

    private final IdGenerator idGenerator;

    /**
     * @param idGenerator източник на ID-та
     */
    public GrammarOperationsService(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Създава нова граматика чийто език е обединението на двата езика
     * @param g1 първа граматика
     * @param g2 втора граматика
     * @return нова граматика за обединението
     */
    public Grammar union(Grammar g1, Grammar g2) {
        Grammar a = copyOf(g1);
        Grammar b = renameToAvoid(g2, namesOf(a));

        Set<String> used = new HashSet<>(namesOf(a));
        used.addAll(namesOf(b));

        NonTerminal newStart = freshNonTerminal("S", used);
        Grammar result = new Grammar(idGenerator.nextId(),
                "Union(" + g1.getName() + "," + g2.getName() + ")", newStart);

        result.addRule(new Rule(newStart, List.of(a.getStartSymbol())));
        result.addRule(new Rule(newStart, List.of(b.getStartSymbol())));
        a.getRules().forEach(result::addRule);
        b.getRules().forEach(result::addRule);
        return result;
    }

    /**
     * Създава нова граматика чийто език е конкатенацията на двата езика
     * @param g1 първа граматика
     * @param g2 втора граматика
     * @return нова граматика за конкатенацията
     */
    public Grammar concat(Grammar g1, Grammar g2) {
        Grammar a = copyOf(g1);
        Grammar b = renameToAvoid(g2, namesOf(a));

        Set<String> used = new HashSet<>(namesOf(a));
        used.addAll(namesOf(b));

        NonTerminal newStart = freshNonTerminal("S", used);
        Grammar result = new Grammar(idGenerator.nextId(),
                "Concat(" + g1.getName() + "," + g2.getName() + ")", newStart);

        result.addRule(new Rule(newStart, List.of(a.getStartSymbol(), b.getStartSymbol())));
        a.getRules().forEach(result::addRule);
        b.getRules().forEach(result::addRule);
        return result;
    }

    /**
     * Създава нова граматика за итерацията на езика (звезда на Клини)
     * @param g входна граматика
     * @return нова граматика за L(g)*
     */
    public Grammar iter(Grammar g) {
        Grammar a = copyOf(g);
        Set<String> used = namesOf(a);
        NonTerminal newStart = freshNonTerminal("S", used);

        Grammar result = new Grammar(idGenerator.nextId(), "Iter(" + g.getName() + ")", newStart);
        // S' -> S S'
        result.addRule(new Rule(newStart, List.of(a.getStartSymbol(), newStart)));
        // S' -> ε
        result.addRule(new Rule(newStart, List.of()));
        a.getRules().forEach(result::addRule);
        return result;
    }

    /**
     * Проверява дали езикът на граматиката е празен
     * Езикът е празен ако стартовият символ не е продуктивен
     * @param g входна граматика
     * @return true ако езикът е празен
     */
    public boolean isLanguageEmpty(Grammar g) {
        Set<NonTerminal> productive = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule r : g.getRules()) {
                if (productive.contains(r.getLeft())) {
                    continue;
                }
                if (allProductive(r.getRight(), productive)) {
                    productive.add(r.getLeft());
                    changed = true;
                }
            }
        }
        return !productive.contains(g.getStartSymbol());
    }

    /**
     * Проверява дали всички символи от дясната страна са продуктивни
     */
    private boolean allProductive(List<Symbol> symbols, Set<NonTerminal> productive) {
        for (Symbol s : symbols) {
            if (s.isTerminal()) {
                continue;
            }
            if (!productive.contains((NonTerminal) s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Прави копие на граматика с ново ID
     */
    private Grammar copyOf(Grammar g) {
        return g.copy(idGenerator.nextId());
    }

    /**
     * Връща имената на всички нетерминали в граматиката
     */
    private Set<String> namesOf(Grammar g) {
        Set<String> out = new LinkedHashSet<>();
        for (NonTerminal nt : g.getNonTerminals()) {
            out.add(nt.getValue());
        }
        return out;
    }

    /**
     * Генерира нов нетерминал чието име не се среща в използваните имена
     */
    private NonTerminal freshNonTerminal(String baseName, Set<String> used) {
        if (!used.contains(baseName)) {
            used.add(baseName);
            return new NonTerminal(baseName);
        }
        int i = 0;
        while (true) {
            String candidate = baseName + i;
            if (!used.contains(candidate)) {
                used.add(candidate);
                return new NonTerminal(candidate);
            }
            i++;
        }
    }

    /**
     * Преименува нетерминалите на граматиката за да избегне конфликти с използваните имена
     */
    private Grammar renameToAvoid(Grammar g, Set<String> usedNames) {
        Set<String> currentUsed = new HashSet<>(usedNames);
        Map<NonTerminal, NonTerminal> rename = new HashMap<>();

        for (NonTerminal nt : g.getNonTerminals()) {
            if (!currentUsed.contains(nt.getValue())) {
                rename.put(nt, nt);
                currentUsed.add(nt.getValue());
            } else {
                NonTerminal fresh = freshNonTerminal(nt.getValue(), currentUsed);
                rename.put(nt, fresh);
            }
        }

        NonTerminal newStart = rename.get(g.getStartSymbol());
        Grammar renamed = new Grammar(idGenerator.nextId(), g.getName() + "'", newStart);
        for (Rule r : g.getRules()) {
            NonTerminal newLeft = rename.get(r.getLeft());
            List<Symbol> newRight = new ArrayList<>();
            for (Symbol s : r.getRight()) {
                if (s.isNonTerminal()) {
                    newRight.add(rename.get((NonTerminal) s));
                } else {
                    newRight.add(s);
                }
            }
            renamed.addRule(new Rule(newLeft, newRight));
        }
        return renamed;
    }
}