package bg.tu_varna.sit.f24621674.exception;

/**
 * Възниква при грешка по време на четене на файл с граматика
 */
public class ParseException extends CfgException {

    /**
     * @param message описание на проблема
     */
    public ParseException(String message) {
        super("Грешка при разчитане: " + message);
    }
}