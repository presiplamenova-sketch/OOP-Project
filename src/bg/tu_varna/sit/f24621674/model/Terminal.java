package bg.tu_varna.sit.f24621674.model;


import bg.tu_varna.sit.f24621674.exception.InvalidRuleException;
import bg.tu_varna.sit.f24621674.util.SymbolUtils;

import java.util.Objects;

/**
 * Терминал в контекстно-свободна граматика.
 *
 * <p>Класът е <b>final</b> и <b>имутабилен</b> – веднъж създаден терминал не се
 * променя, защото се сравнява често по equals/hashCode (използва се в Set/Map).</p>
 */
public final class Terminal implements Symbol {

    private final String value;

    /**
     * Създава нов терминал след валидация.
     *
     * @param value стойност, която трябва да се състои само от малки букви или цифри
     * @throws InvalidRuleException ако стойността не е валиден терминал
     */
    public Terminal(String value) {
        Objects.requireNonNull(value, "Стойността на терминал не може да е null.");
        if (!SymbolUtils.isTerminalToken(value)) {
            throw new InvalidRuleException(
                    "'" + value + "' не е валиден терминал – позволени са малки букви и цифри.");
        }
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Terminal t && t.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
