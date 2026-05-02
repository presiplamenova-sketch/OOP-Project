package bg.tu_varna.sit.f24621674.model;


import bg.tu_varna.sit.f24621674.exception.InvalidRuleException;
import bg.tu_varna.sit.f24621674.util.SymbolUtils;

import java.util.Objects;

/**
 * Нетерминал (променлива) в контекстно-свободна граматика.
 *
 * <p>Първоначалните нетерминали, идващи от потребителя, са единични главни
 * латински букви. При вътрешни преобразувания (например CNF или union) може
 * да се генерират имена с повече символи – затова класът съхранява
 * <code>String</code>, а не <code>char</code>.</p>
 */
public final class NonTerminal implements Symbol {

    private final String name;

    /**
     * Създава нетерминал след валидация.
     *
     * @param name име (започва с главна буква, следвано от букви/цифри)
     * @throws InvalidRuleException ако името не е валидно
     */
    public NonTerminal(String name) {
        Objects.requireNonNull(name, "Името на нетерминал не може да е null.");
        if (!SymbolUtils.isNonTerminalToken(name)) {
            throw new InvalidRuleException(
                    "'" + name + "' не е валиден нетерминал – трябва да започва с главна буква.");
        }
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NonTerminal n && n.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
