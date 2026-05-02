package bg.tu_varna.sit.f24621674.util;


/**
 * Помощни методи за работа със символи – дали са терминали, нетерминали и т.н.
 *
 * <p>В контекстно-свободната граматика, която поддържаме:</p>
 * <ul>
 *   <li>главните латински букви (A–Z) са нетерминали;</li>
 *   <li>малките латински букви (a–z) и цифрите (0–9) са терминали.</li>
 * </ul>
 *
 * <p>Вътрешно (например при преобразуване до НФЧ) генерираме нетерминали
 * с многосимволни имена като <code>X1</code>, <code>S0</code> – първият им
 * символ обаче винаги е главна буква.</p>
 */
public final class SymbolUtils {

    /** Класически маркер за празната дума – позволени са и "eps", "epsilon", "_". */
    public static final String EPSILON = "ε";

    private SymbolUtils() {
        // помощен клас, не се инстанцира
    }

    /**
     * Проверява дали един символ е валиден като терминал (малка буква или цифра).
     *
     * @param c знак за проверка
     * @return true ако е валиден терминал
     */
    public static boolean isTerminalChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }

    /**
     * Проверява дали един символ е валиден като нетерминал (главна латинска буква).
     *
     * @param c знак за проверка
     * @return true ако е валиден нетерминал
     */
    public static boolean isNonTerminalChar(char c) {
        return c >= 'A' && c <= 'Z';
    }

    /**
     * Проверява дали даден токен (низ) е валиден като нетерминално име.
     *
     * <p>Изискване: започва с главна буква, останалите символи са букви или цифри.</p>
     *
     * @param token входен низ
     * @return true ако е валиден нетерминал
     */
    public static boolean isNonTerminalToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        if (!isNonTerminalChar(token.charAt(0))) {
            return false;
        }
        for (int i = 1; i < token.length(); i++) {
            char c = token.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверява дали даден токен е валиден като терминално име – всички символи
     * трябва да са малки латински букви или цифри.
     *
     * @param token входен низ
     * @return true ако е валиден терминал
     */
    public static boolean isTerminalToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        for (int i = 0; i < token.length(); i++) {
            if (!isTerminalChar(token.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверява дали даден низ представлява "празна дума" (epsilon).
     *
     * @param token входен низ
     * @return true ако токенът означава epsilon
     */
    public static boolean isEpsilonToken(String token) {
        if (token == null) {
            return false;
        }
        String t = token.trim();
        return t.isEmpty()
                || t.equals(EPSILON)
                || t.equalsIgnoreCase("eps")
                || t.equalsIgnoreCase("epsilon")
                || t.equals("_");
    }
}
