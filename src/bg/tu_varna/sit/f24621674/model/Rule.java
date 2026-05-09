package bg.tu_varna.sit.f24621674.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Едно правило в граматика - нетерминал от ляво и списък от символи отдясно
 * Празната дясна страна означава epsilon-продукция
 */
public final class Rule {

    private final NonTerminal left;
    private final List<Symbol> right;

    /**
     * Създава ново правило
     * @param left лявата страна - нетерминал
     * @param right дясната страна - списък от символи (може да е празен за epsilon)
     */
    public Rule(NonTerminal left, List<Symbol> right) {
        this.left = Objects.requireNonNull(left, "Лявата страна не може да е null");
        Objects.requireNonNull(right, "Дясната страна не може да е null");
        this.right = Collections.unmodifiableList(new ArrayList<>(right));
    }

    /**
     * @return лявата страна на правилото
     */
    public NonTerminal getLeft() {
        return left;
    }

    /**
     * @return дясната страна на правилото
     */
    public List<Symbol> getRight() {
        return right;
    }

    /**
     * @return true ако правилото е epsilon-продукция
     */
    public boolean isEpsilon() {
        return right.isEmpty();
    }

    /** Връща текстово представяне от вида A -> a B C  */
    @Override
    public String toString() {
        String rightStr = right.isEmpty()
                ? "ε"
                : right.stream().map(Symbol::getValue).collect(Collectors.joining(" "));
        return left.getValue() + " -> " + rightStr;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rule r)) {
            return false;
        }
        return r.left.equals(this.left) && r.right.equals(this.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}