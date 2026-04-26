package bg.tu_varna.sit.f24621674.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Едно правило в граматика – нетерминал от ляво и списък от символи отдясно.
 *
 * <p>Празният списък на дясната страна означава epsilon-продукция
 * (<code>A → ε</code>). Правилото е имутабилно; добавянето и премахването на
 * правила става на ниво {@link Grammar}, а не чрез мутация на самия обект.</p>
 */
public final class Rule {

    private final NonTerminal left;
    private final List<Symbol> right;

    /**
     * Конструктор.
     *
     * @param left лявата страна – нетерминал (не може да е null)
     * @param right дясната страна – списък от символи (може да е празен = epsilon)
     */
    public Rule(NonTerminal left, List<Symbol> right) {
        this.left = Objects.requireNonNull(left, "Лявата страна не може да е null.");
        Objects.requireNonNull(right, "Дясната страна не може да е null – празен списък е ОК.");
        // правим дефанзивно копие – така никой отвън не може да мутира вътрешния списък
        this.right = Collections.unmodifiableList(new ArrayList<>(right));
    }

    /**
     * @return лявата страна на правилото
     */
    public NonTerminal getLeft() {
        return left;
    }

    /**
     * @return дясната страна на правилото (имутабилен списък)
     */
    public List<Symbol> getRight() {
        return right;
    }

    /**
     * @return true ако правилото е epsilon-продукция (дясна страна = празен списък)
     */
    public boolean isEpsilon() {
        return right.isEmpty();
    }

    /**
     * Текстово представяне от вида <code>A -&gt; a B C</code>. Символите от
     * дясната страна се разделят с интервали за по-лесно четене при
     * многосимволни нетерминали.
     *
     * @return човешко-четимо представяне на правилото
     */
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
