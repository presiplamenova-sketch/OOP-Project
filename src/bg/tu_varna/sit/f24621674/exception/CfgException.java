package bg.tu_varna.sit.f24621674.exception;

/** Базов клас за всички изключения в проекта */
public class CfgException extends RuntimeException {

    /**
     * Създава изключение със съобщение
     * @param message съобщение за грешката
     */
    public CfgException(String message) {
        super(message);
    }

    /**
     * Създава изключение със съобщение и първопричина
     * @param message съобщение за грешката
     * @param cause първопричината за грешката
     */
    public CfgException(String message, Throwable cause) {
        super(message, cause);
    }
}