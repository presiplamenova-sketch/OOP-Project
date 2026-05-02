package bg.tu_varna.sit.f24621674.command;

import bg.tu_varna.sit.f24621674.command.registry.CommandRegistry;
import bg.tu_varna.sit.f24621674.service.ChomskyService;
import bg.tu_varna.sit.f24621674.service.CykService;
import bg.tu_varna.sit.f24621674.service.GrammarOperationsService;
import bg.tu_varna.sit.f24621674.service.GrammarService;
import bg.tu_varna.sit.f24621674.storage.FileGrammarStorage;
import bg.tu_varna.sit.f24621674.storage.GrammarRepository;
import bg.tu_varna.sit.f24621674.util.ConsoleIO;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Контекстът, който всяка команда получава при изпълнение.
 *
 * <p>Съдържа всички инжектирани зависимости (services, repository, console)
 * плюс състоянието, което е "общосесийно" – напр. кой е текущо отвореният файл.
 * Така всяка команда остава малка и не строи сама service-ите.</p>
 *
 * <p>Това е умишлено <b>не-god-class</b>: класът само държи препратки,
 * не прави нищо друго.</p>
 */
public class CommandContext {

    private final GrammarRepository repository;
    private final FileGrammarStorage fileStorage;
    private final GrammarService grammarService;
    private final GrammarOperationsService operationsService;
    private final ChomskyService chomskyService;
    private final CykService cykService;
    private final ConsoleIO console;
    private final CommandRegistry commandRegistry;

    private Path currentFile;
    private boolean exitRequested;

    /**
     * Конструкторът приема всички зависимости наведнъж.
     * Построяването става централно в {@link bg.uni.fmi.cfg.app.ApplicationBootstrap}.
     *
     * @param repository хранилището на граматики
     * @param fileStorage слоят за файлово четене/писане
     * @param grammarService услуги за печат/форматиране
     * @param operationsService union/concat/iter/empty
     * @param chomskyService CNF операции
     * @param cykService CYK алгоритъм
     * @param console обвивка на конзолата
     * @param commandRegistry регистър на всички команди (нужен е на HelpCommand)
     */
    public CommandContext(GrammarRepository repository,
                          FileGrammarStorage fileStorage,
                          GrammarService grammarService,
                          GrammarOperationsService operationsService,
                          ChomskyService chomskyService,
                          CykService cykService,
                          ConsoleIO console,
                          CommandRegistry commandRegistry) {
        this.repository = repository;
        this.fileStorage = fileStorage;
        this.grammarService = grammarService;
        this.operationsService = operationsService;
        this.chomskyService = chomskyService;
        this.cykService = cykService;
        this.console = console;
        this.commandRegistry = commandRegistry;
    }

    /** @return хранилището с граматики */
    public GrammarRepository getRepository() {
        return repository;
    }

    /** @return слой за работа с файлове */
    public FileGrammarStorage getFileStorage() {
        return fileStorage;
    }

    /** @return общ сервиз за граматики (печат и т.н.) */
    public GrammarService getGrammarService() {
        return grammarService;
    }

    /** @return сервиз за union/concat/iter/empty */
    public GrammarOperationsService getOperationsService() {
        return operationsService;
    }

    /** @return сервиз за CNF операции */
    public ChomskyService getChomskyService() {
        return chomskyService;
    }

    /** @return сервиз за CYK */
    public CykService getCykService() {
        return cykService;
    }

    /** @return конзолен IO */
    public ConsoleIO getConsole() {
        return console;
    }

    /** @return регистърът с команди (нужен е само на HelpCommand) */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /** @return текущо отворения файл, ако има такъв */
    public Optional<Path> getCurrentFile() {
        return Optional.ofNullable(currentFile);
    }

    /**
     * Задава текущия файл – извиква се от open/save as.
     * @param file пътят до файла
     */
    public void setCurrentFile(Path file) {
        this.currentFile = file;
    }

    /**
     * Премахва маркера за "отворен файл".
     */
    public void clearCurrentFile() {
        this.currentFile = null;
    }

    /** @return true ако има отворен файл */
    public boolean isFileOpen() {
        return currentFile != null;
    }

    /** @return true ако е подадена команда <code>exit</code> */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /**
     * Маркира за излизане – CLI engine ще завърши главния цикъл.
     */
    public void requestExit() {
        this.exitRequested = true;
    }
}
