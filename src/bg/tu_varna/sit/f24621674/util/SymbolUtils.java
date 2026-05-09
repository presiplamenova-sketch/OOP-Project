package bg.tu_varna.sit.f24621674.util;

/**
 * Помощни методи за проверка на символи в граматика
 * Главни букви A-Z са нетерминали
 * Малки букви a-z и цифри 0-9 са терминали
 */
public final class SymbolUtils {

    /** Маркер за празната дума - позволени са и eps epsilon и _ */
    public static final String EPSILON = "ε";

    private SymbolUtils() {
        // помощен клас не се инстанцира
    }

    /**
     * Проверява дали символът е валиден терминал
     * @param c символът за проверка
     * @return true ако е малка буква или цифра
     */
    public static boolean isTerminalChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }

    /**
     * Проверява дали символът е валиден нетерминал
     * @param c символът за проверка
     * @return true ако е главна латинска буква
     */
    public static boolean isNonTerminalChar(char c) {
        return c >= 'A' && c <= 'Z';
    }

    /**
     * Проверява дали низът е валидно нетерминално име
     * Трябва да започва с главна буква следвана от букви или цифри
     * @param token низът за проверка
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
     * Проверява дали низът е валидно терминално име
     * Всички символи трябва да са малки букви или цифри
     * @param token низът за проверка
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
     * Проверява дали низът представлява празна дума
     * @param token низът за проверка
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