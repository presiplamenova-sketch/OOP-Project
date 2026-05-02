package bg.tu_varna.sit.f24621674.app;


/**
 * Входна точка на приложението.
 *
 * <p>Умишлено държим класа минимален – цялата логика по сглобяване е в
 * {@link ApplicationBootstrap}. Това значи, че <code>Main</code> няма
 * никакви зависимости към конкретни команди/сервизи, а е само "стартер".</p>
 */
public final class Main {

    private Main() {
        // статичен вход – не се инстанцира
    }

    /**
     * Главен метод. Не приема аргументи – цялото взаимодействие става
     * интерактивно през конзолата.
     *
     * @param args аргументи на командния ред (игнорират се)
     */
    public static void main(String[] args) {
        new ApplicationBootstrap().bootstrap().run();
    }
}

