package bg.tu_varna.sit.f24621674.exception;


/**
 * Хвърля се когато потребителят е въвел непозната команда или
 * грешен брой/тип аргументи към съществуваща команда.
 */
public class InvalidCommandException extends CfgException {

    /**
     * @param message човешко-четимо описание на проблема
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
