package bg.tu_varna.sit.f24621674.exception;

/**
 * Хвърля се при грешка по време на парсване на файл с граматика
 * (липсваща секция, неочакван токен, повредена структура и т.н.).
 */
public class ParseException extends CfgException {

    /**
     * @param message подробности за проблема
     */
    public ParseException(String message) {
        super("Грешка при парсване: " + message);
    }
}

