package bg.tu_varna.sit.f24621674.command;


/**
 * Резултат от изпълнение на команда.
 *
 * <p>Използва се за структуриран отговор – успех/грешка + съобщение.
 * {@link bg.tu_varna.sit.f24621674.command} може да реши какво да
 * направи с резултата (принципно само го принтира).</p>
 */
public final class CommandResult {

    private final boolean success;
    private final String message;

    private CommandResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Фабричен метод за успешен резултат.
     *
     * @param message съобщение (може да е празно ако командата е "тиха")
     * @return нов резултат
     */
    public static CommandResult ok(String message) {
        return new CommandResult(true, message);
    }

    /**
     * Фабричен метод за успех без съобщение.
     *
     * @return нов резултат
     */
    public static CommandResult ok() {
        return new CommandResult(true, "");
    }

    /**
     * Фабричен метод за неуспешен резултат.
     *
     * @param message причина за грешката
     * @return нов резултат
     */
    public static CommandResult error(String message) {
        return new CommandResult(false, message);
    }

    /** @return true ако е успех */
    public boolean isSuccess() {
        return success;
    }

    /** @return съобщението на резултата */
    public String getMessage() {
        return message;
    }
}

