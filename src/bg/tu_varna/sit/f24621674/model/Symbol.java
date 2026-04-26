package bg.tu_varna.sit.f24621674.model;

/**
 * Абстракция на символ от азбуката на граматиката.
 *
 * <p>Символите биват два вида:</p>
 * <ul>
 *   <li>{@link Terminal} – малки латински букви и цифри;</li>
 *   <li>{@link NonTerminal} – главни латински букви (или генерирани имена).</li>
 * </ul>
 *
 * <p>Интерфейсът е sealed – умишлено ограничаваме подтиповете, за да няма
 * случайни "трети видове" символи, които да разбият алгоритмите.</p>
 */
public sealed interface Symbol permits Terminal, NonTerminal {

    /**
     * @return текстовото представяне на символа (напр. "a", "S", "X1")
     */
    String getValue();

    /**
     * @return true ако символът е терминал, false ако е нетерминал
     */
    boolean isTerminal();

    /**
     * @return обратното на {@link #isTerminal()}
     */
    default boolean isNonTerminal() {
        return !isTerminal();
    }
}
