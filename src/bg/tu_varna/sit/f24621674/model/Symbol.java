package bg.tu_varna.sit.f24621674.model;

/**
 * Символ от азбуката на граматиката
 * Два вида: Terminal (малки букви и цифри) и NonTerminal (главни букви)
 */
public sealed interface Symbol permits Terminal, NonTerminal {

    /**
     * @return текстовото представяне на символа
     */
    String getValue();

    /**
     * @return true ако символът е терминал
     */
    boolean isTerminal();

    /**
     * @return true ако символът е нетерминал
     */
    default boolean isNonTerminal() {
        return !isTerminal();
    }
}