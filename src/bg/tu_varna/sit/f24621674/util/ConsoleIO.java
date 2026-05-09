package bg.tu_varna.sit.f24621674.util;

import java.io.PrintStream;

/**
 * Обвивка около System.out и System.err за извеждане на съобщения
 * Командите използват този клас вместо да извикват System.out директно
 */
public class ConsoleIO {

    private final PrintStream out;
    private final PrintStream err;

    /**
     * Създава ConsolIO със стандартните потоци stdout и stderr
     */
    public ConsoleIO() {
        this(System.out, System.err);
    }

    /**
     * Създава ConsoleIO с персонализирани потоци
     * @param out стандартен изход
     * @param err поток за грешки
     */
    public ConsoleIO(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    /**
     * Извежда съобщение с нов ред
     * @param message съобщението за извеждане
     */
    public void println(String message) {
        out.println(message);
    }

    /**
     * Извежда съобщение без нов ред
     * @param message съобщението за извеждане
     */
    public void print(String message) {
        out.print(message);
    }

    /**
     * Извежда съобщение за грешка
     * @param message съобщението за грешката
     */
    public void error(String message) {
        err.println("[ГРЕШКА] " + message);
    }

    /**
     * Извежда информационно съобщение
     * @param message съобщението за извеждане
     */
    public void info(String message) {
        out.println("[ИНФО] " + message);
    }
}