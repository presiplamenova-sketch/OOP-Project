package bg.tu_varna.sit.f24621674.exception;


/**
 * Базов клас за всички специфични изключения в проекта.
 *
 * <p>Идеята е да имаме обща йерархия, така че горните слоеве (напр. командите и
 * CLI engine-ът) да могат да хващат само една обща класа изключения и да показват
 * разумно съобщение към потребителя, вместо да работят с <code>Exception</code> директно.</p>
 */
public class CfgException extends RuntimeException {

    /**
     * Създава ново изключение със съобщение.
     *
     * @param message четимо съобщение за грешката на български
     */
    public CfgException(String message) {
        super(message);
    }

    /**
     * Създава ново изключение със съобщение и причина.
     *
     * @param message съобщение
     * @param cause оригиналната причина
     */
    public CfgException(String message, Throwable cause) {
        super(message, cause);
    }
}
