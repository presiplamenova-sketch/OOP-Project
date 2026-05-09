package bg.tu_varna.sit.f24621674.command;

/**
 * Резултат от изпълнение на команда
 * Съдържа информация дали командата е успешна и съобщение за потребителя
 */
public final class CommandResult {

    private final boolean success;
    private final String message;

    private CommandResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Създава успешен резултат със съобщение
     * @param message съобщение за потребителя
     * @return нов резултат
     */
    public static CommandResult ok(String message) {
        return new CommandResult(true, message);
    }

    /**
     * Създава успешен резултат без съобщение
     * @return нов резултат
     */
    public static CommandResult ok() {
        return new CommandResult(true, "");
    }

    /**
     * Създава неуспешен резултат със съобщение за грешка
     * @param message причина за грешката
     * @return нов резултат
     */
    public static CommandResult error(String message) {
        return new CommandResult(false, message);
    }

    /** @return true ако командата е изпълнена успешно  */
    public boolean isSuccess() {
        return success;
    }

    /** @return съобщението на резултата  */
    public String getMessage() {
        return message;
    }
}