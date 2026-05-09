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
 * Контекст който всяка команда получава при изпълнение
 * Съдържа всички сервизи и текущото състояние на сесията
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
     * Приема всички зависимости наведнъж
     *
     * @param repository хранилището на граматики
     * @param fileStorage слой за файлово четене и писане
     * @param grammarService сервиз за печат и форматиране
     * @param operationsService сервиз за union concat iter empty
     * @param chomskyService сервиз за НФЧ операции
     * @param cykService сервиз за CYK алгоритъм
     * @param console обвивка на конзолата
     * @param commandRegistry регистър на всички команди
     */
    public CommandContext(GrammarRepository repository, FileGrammarStorage fileStorage,
    GrammarService grammarService, GrammarOperationsService operationsService, ChomskyService chomskyService,
    CykService cykService, ConsoleIO console, CommandRegistry commandRegistry) {
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

    /** @return сервиз за форматиране на граматики  */
    public GrammarService getGrammarService() {
        return grammarService;
    }

    /** @return сервиз за union concat iter empty */
    public GrammarOperationsService getOperationsService() {
        return operationsService;
    }

    /** @return сервиз за НФЧ операции */
    public ChomskyService getChomskyService() {
        return chomskyService;
    }

    /** @return сервиз за CYK алгоритъм */
    public CykService getCykService() {
        return cykService;
    }

    /** @return конзолен IO  */
    public ConsoleIO getConsole() {
        return console;
    }

    /** @return регистърът с всички команди */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /** @return текущо отворения файл ако има такъв  */
    public Optional<Path> getCurrentFile() {
        return Optional.ofNullable(currentFile);
    }

    /** Задава текущия файл @param file пътят до файла  */
    public void setCurrentFile(Path file) {
        this.currentFile = file;
    }

    /** Премахва текущия файл   */
    public void clearCurrentFile() {
        this.currentFile = null;
    }

    /** @return true ако има отворен файл  */
    public boolean isFileOpen() {
        return currentFile != null;
    }

    /** @return true ако е подадена команда exit  */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /** Маркира програмата за излизане  */
    public void requestExit() {
        this.exitRequested = true;
    }
}