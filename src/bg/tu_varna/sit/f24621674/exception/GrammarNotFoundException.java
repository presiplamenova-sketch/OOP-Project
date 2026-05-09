package bg.tu_varna.sit.f24621674.exception;

/**
 * Възниква когато граматика с търсеното ID не съществува
 */
public class GrammarNotFoundException extends CfgException {

    /**
     * @param id липсващото ID
     */
    public GrammarNotFoundException(int id) {
        super("Не съществува граматика с ID " + id);
    }
}