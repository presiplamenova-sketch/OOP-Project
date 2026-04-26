package bg.tu_varna.sit.f24621674.exception;

/**
 * Хвърля се при проблеми с файловата система при четене или запис на граматики.
 *
 * <p>Замисълът е да обвиваме нискониво I/O изключения и да даваме на
 * потребителя по-разбираемо съобщение на български.</p>
 */
public class StorageException extends CfgException {

    /**
     * @param message съобщение
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * @param message съобщение
     * @param cause оригиналното изключение
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
