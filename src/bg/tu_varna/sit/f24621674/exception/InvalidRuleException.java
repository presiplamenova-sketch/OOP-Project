package bg.tu_varna.sit.f24621674.exception;


/**
 * Хвърля се когато низ, който трябва да представя правило, не може да бъде парснат.
 *
 * <p>Примери: липсва оператор <code>-&gt;</code>, лявата страна не е нетерминал,
 * присъстват символи извън позволената азбука и т.н.</p>
 */
public class InvalidRuleException extends CfgException {

    /**
     * @param message детайлно описание защо правилото е невалидно
     */
    public InvalidRuleException(String message) {
        super("Невалидно правило: " + message);
    }
}
