package bg.tu_varna.sit.f24621674.model;

import bg.tu_varna.sit.f24621674.exception.InvalidRuleException;
import bg.tu_varna.sit.f24621674.util.SymbolUtils;

import java.util.Objects;

/**
 * Нетерминал в контекстно-свободна граматика
 * Името започва с главна буква следвана от букви или цифри
 */
public final class NonTerminal implements Symbol {

    private final String name;

    /**
     * Създава нетерминал след валидация на името
     * @param name име на нетерминала
     */
    public NonTerminal(String name) {
        Objects.requireNonNull(name, "Името на нетерминал не може да е null");
        if (!SymbolUtils.isNonTerminalToken(name)) {
            throw new InvalidRuleException(
                    "'" + name + "' не е валиден нетерминал - трябва да започва с главна буква");
        }
        this.name = name;
    }

    @Override
    public String getValue() {
        return name;
    }

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