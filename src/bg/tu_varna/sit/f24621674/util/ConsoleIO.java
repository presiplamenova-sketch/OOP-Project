package bg.tu_varna.sit.f24621674.util;


import java.io.PrintStream;

/**
 * Тънка обвивка около <code>System.out</code> / <code>System.err</code>.
 *
 * <p>Смисълът е командите да не зависят директно от <code>System.out</code>,
 * а да получават (или използват) обща точка за печат – така по-лесно може
 * да се тества и да се заменя изходът, ако потрябва.</p>
 */
public class ConsoleIO {

    private final PrintStream out;
    private final PrintStream err;

    /**
     * Конструктор с дефолтни потоци (stdout, stderr).
     */
    public ConsoleIO() {
        this(System.out, System.err);
    }

    /**
     * Конструктор с персонализирани потоци – използва се в тестове.
     *
     * @param out стандартен изход
     * @param err поток за грешки
     */
    public ConsoleIO(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    /**
     * Извежда съобщение с нов ред.
     *
     * @param message съобщение
     */
    public void println(String message) {
        out.println(message);
    }

    /**
     * Извежда съобщение без нов ред (удобно за промпт).
     *
     * @param message съобщение
     */
    public void print(String message) {
        out.print(message);
    }

    /**
     * Извежда съобщение за грешка на червения поток.
     *
     * @param message съобщение за грешка
     */
    public void error(String message) {
        err.println("[ГРЕШКА] " + message);
    }

    /**
     * Извежда информационно съобщение с префикс.
     *
     * @param message съобщение
     */
    public void info(String message) {
        out.println("[ИНФО] " + message);
    }
}
