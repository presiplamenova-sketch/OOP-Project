package bg.tu_varna.sit.f24621674.model;

import bg.tu_varna.sit.f24621674.model.NonTerminal;
import bg.tu_varna.sit.f24621674.model.Symbol;
import bg.tu_varna.sit.f24621674.model.Terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Контекстно-свободна граматика – централната структура в проекта.
 *
 * <p>Граматиката има уникално числово ID (давано от
 * {@link bg.tu_varna.sit.f24621674.model}), име, стартов нетерминал и
 * подреден списък от правила. Терминалите и нетерминалите се изчисляват
 * "лениво" от правилата плюс стартовия символ – така няма дублиране на
 * информация и не може да се изпадне в несъгласувано състояние.</p>
 *
 * <p>Класът е <b>мутабилен</b> по отношение на правилата (нужно за
 * <code>addRule</code>/<code>removeRule</code>), но поддържа и
 * {@link #copy(int)} за случаи, когато service слоят строи нова граматика
 * (union, concat, chomskify и т.н.).</p>
 */
public class Grammar {

    private final int id;
    private String name;
    private NonTerminal startSymbol;
    private final List<Rule> rules;

    /**
     * Създава нова граматика.
     *
     * @param id уникален идентификатор
     * @param name име на граматиката (за визуализация)
     * @param startSymbol стартов нетерминал
     */
    public Grammar(int id, String name, NonTerminal startSymbol) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Името не може да е null.");
        this.startSymbol = Objects.requireNonNull(startSymbol, "Стартов символ е задължителен.");
        this.rules = new ArrayList<>();
    }

    /** @return уникалното ID */
    public int getId() {
        return id;
    }

    /** @return името на граматиката */
    public String getName() {
        return name;
    }

    /**
     * Сменя името на граматиката (използва се при операции, които произвеждат нова).
     * @param name ново име
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    /** @return стартовият нетерминал */
    public NonTerminal getStartSymbol() {
        return startSymbol;
    }

    /**
     * Подменя стартовия нетерминал. Полезно при CNF преобразуване (нов S0).
     * @param startSymbol новият стартов символ
     */
    public void setStartSymbol(NonTerminal startSymbol) {
        this.startSymbol = Objects.requireNonNull(startSymbol);
    }

    /**
     * @return имутабилен изглед към списъка с правила (в реда на добавяне)
     */
    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    /**
     * Добавя ново правило в края на списъка.
     * Не правим проверка за дубликати – ако потребителят сам си добави
     * едно и също правило два пъти, ще се вижда два пъти при <code>print</code>.
     *
     * @param rule правилото за добавяне
     */
    public void addRule(Rule rule) {
        rules.add(Objects.requireNonNull(rule));
    }

    /**
     * Премахва правилото на дадена 1-базирана позиция.
     *
     * @param oneBasedIndex индекс, започващ от 1 (както го показва <code>print</code>)
     * @return премахнатото правило
     * @throws IndexOutOfBoundsException ако индексът е невалиден
     */
    public Rule removeRule(int oneBasedIndex) {
        if (oneBasedIndex < 1 || oneBasedIndex > rules.size()) {
            throw new IndexOutOfBoundsException(
                    "Няма правило с номер " + oneBasedIndex + " (валидни: 1.." + rules.size() + ").");
        }
        return rules.remove(oneBasedIndex - 1);
    }

    /**
     * Изчислява всички нетерминали, които участват в граматиката – включително
     * стартовия и тези, които се появяват в правилата.
     *
     * @return множество от нетерминали (с предсказуем ред чрез LinkedHashSet)
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
     * Изчислява всички терминали, които се срещат в правилата.
     *
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
     * Прави дълбоко копие на граматиката, но със зададено ново ID.
     * Използва се от operations service (union/concat/iter/chomskify).
     *
     * @param newId новото ID за копието
     * @return нов обект Grammar с идентични правила и стартов символ
     */
    public Grammar copy(int newId) {
        Grammar g = new Grammar(newId, this.name, this.startSymbol);
        for (Rule r : this.rules) {
            g.addRule(new Rule(r.getLeft(), r.getRight()));
        }
        return g;
    }
}
