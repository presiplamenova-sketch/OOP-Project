package bg.tu_varna.sit.f24621674.exception;

/**
 * Хвърля се, когато потребителят се опита да достъпи граматика по ID,
 * което не съществува в репозиторито.
 */
public class GrammarNotFoundException extends CfgException {

    /**
     * @param id липсващото ID
     */
    public GrammarNotFoundException(int id) {
        super("Не съществува граматика с ID " + id + ".");
    }
}
