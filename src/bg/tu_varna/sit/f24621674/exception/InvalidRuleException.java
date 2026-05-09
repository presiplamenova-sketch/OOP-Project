package bg.tu_varna.sit.f24621674.exception;

/**
 * Възниква когато даден низ не може да бъде разпознат като валидно правило
 */
public class InvalidRuleException extends CfgException {

    /**
     * @param message описание защо правилото е невалидно
     */
    public InvalidRuleException(String message) {
        super("Невалидно правило: " + message);
    }
}