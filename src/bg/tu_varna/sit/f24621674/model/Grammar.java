package bg.tu_varna.sit.f24621674.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Контекстно-свободна граматика - централната структура в проекта
 * Съдържа уникално ID, име, стартов нетерминал и списък от правила
 */
public class Grammar {

    private final int id;
    private String name;
    private NonTerminal startSymbol;
    private final List<Rule> rules;

    /**
     * Създава нова граматика
     * @param id уникален идентификатор
     * @param name ime на граматиката
     * @param startSymbol стартов нетерминал
     */
    public Grammar(int id, String name, NonTerminal startSymbol) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Името не може да е null");
        this.startSymbol = Objects.requireNonNull(startSymbol, "Стартов символ е задължителен");
        this.rules = new ArrayList<>();
    }

    /** @return уникалното ID  */
    public int getId() {
        return id;
    }

    /** @return името на граматиката */
    public String getName() {
        return name;
    }

    /**
     * Сменя името на граматиката
     * @param name ново име
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    /** @return стартовият нетерминал  */
    public NonTerminal getStartSymbol() {
        return startSymbol;
    }

    /**
     * Сменя стартовия нетерминал
     * @param startSymbol новият стартов символ
     */
    public void setStartSymbol(NonTerminal startSymbol) {
        this.startSymbol = Objects.requireNonNull(startSymbol);
    }

    /** @return списъкът с правила в реда на добавяне */
    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    /**
     * Добавя ново правило в края на списъка
     * @param rule правилото за добавяне
     */
    public void addRule(Rule rule) {
        rules.add(Objects.requireNonNull(rule));
    }

    /**
     * Премахва правило по неговия номер
     * @param oneBasedIndex номер на правилото започващ от 1
     * @return премахнатото правило
     */
    public Rule removeRule(int oneBasedIndex) {
        if (oneBasedIndex < 1 || oneBasedIndex > rules.size()) {
            throw new IndexOutOfBoundsException(
                    "Няма правило с номер " + oneBasedIndex + " (валидни: 1.." + rules.size() + ")");
        }
        return rules.remove(oneBasedIndex - 1);
    }

    /**
     * Връща всички нетерминали в граматиката
     * @return множество от нетерминали
     */
    public Set<NonTerminal> getNonTerminals() {
        Set<NonTerminal> result = new LinkedHashSet<>();
        result.add(startSymbol);
        for (Rule r : rules) {
            result.add(r.getLeft());
            for (Symbol s : r.getRight()) {
                if (s.isNonTerminal()) {
                    result.add((NonTerminal) s);
                }
            }
        }
        return result;
    }

    /**
     * Връща всички терминали в граматиката
     * @return множество от терминали
     */
    public Set<Terminal> getTerminals() {
        Set<Terminal> result = new LinkedHashSet<>();
        for (Rule r : rules) {
            for (Symbol s : r.getRight()) {
                if (s.isTerminal()) {
                    result.add((Terminal) s);
                }
            }
        }
        return result;
    }

    /**
     * Прави копие на граматиката с ново ID
     * @param newId новото ID за копието
     * @return нова граматика с идентични правила
     */
    public Grammar copy(int newId) {
        Grammar g = new Grammar(newId, this.name, this.startSymbol);
        for (Rule r : this.rules) {
            g.addRule(new Rule(r.getLeft(), r.getRight()));
        }
        return g;
    }
}