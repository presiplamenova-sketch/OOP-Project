package bg.tu_varna.sit.f24621674.exception;

/**
 * Възниква при непозната команда или грешни аргументи
 */
public class InvalidCommandException extends CfgException {

    /**
     * @param message описание на проблема
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}