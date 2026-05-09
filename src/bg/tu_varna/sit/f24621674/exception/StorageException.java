package bg.tu_varna.sit.f24621674.exception;

/**
 * Възниква при проблем с четене или запис на файл
 */
public class StorageException extends CfgException {

    /**
     * @param message описание на проблема
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * @param message описание на проблема
     * @param cause първопричината за грешката
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}