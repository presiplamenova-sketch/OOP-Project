package bg.tu_varna.sit.f24621674.app;

import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.impl.*;
import bg.tu_varna.sit.f24621674.command.registry.CommandRegistry;
import bg.tu_varna.sit.f24621674.parser.CommandLineParser;
import bg.tu_varna.sit.f24621674.parser.GrammarFileParser;
import bg.tu_varna.sit.f24621674.parser.RuleParser;
import bg.tu_varna.sit.f24621674.service.ChomskyService;
import bg.tu_varna.sit.f24621674.service.CykService;
import bg.tu_varna.sit.f24621674.service.GrammarOperationsService;
import bg.tu_varna.sit.f24621674.service.GrammarService;
import bg.tu_varna.sit.f24621674.storage.FileGrammarStorage;
import bg.tu_varna.sit.f24621674.storage.GrammarRepository;
import bg.tu_varna.sit.f24621674.util.ConsoleIO;

/**
 * Главен входен клас на приложението.
 * Инициализира всички зависимости и стартира командния интерфейс.
 */
public final class Main {

    /**
     * Частен конструктор.
     * Класът не трябва да се инстанцира.
     */
    private Main() {
        // статичен вход – не се инстанцира
    }

    /**
     * Главен метод на приложението.
     * Цялото взаимодействие се извършва интерактивно през конзолата.
     *
     * @param args аргументи от командния ред (не се използват)
     */
    public static void main(String[] args) {

        // Инициализация
        ConsoleIO console = new ConsoleIO();
        RuleParser ruleParser = new RuleParser();
        GrammarFileParser grammarFileParser = new GrammarFileParser(ruleParser);
        GrammarRepository repository = new GrammarRepository();
        FileGrammarStorage storage = new FileGrammarStorage(grammarFileParser);

        GrammarService grammarService = new GrammarService();
        GrammarOperationsService operationsService =
                new GrammarOperationsService(repository.getIdGenerator());

        ChomskyService chomskyService =
                new ChomskyService(repository.getIdGenerator());

        CykService cykService = new CykService(chomskyService);

        // Регистриране на командите
        CommandRegistry registry = new CommandRegistry();

        registry.register(new HelpCommand());
        registry.register(new OpenCommand());
        registry.register(new CloseCommand());
        registry.register(new SaveCommand());
        registry.register(new SaveAsCommand());
        registry.register(new ExitCommand());

        registry.register(new ListCommand());
        registry.register(new PrintCommand());

        registry.register(new AddRuleCommand(ruleParser));
        registry.register(new RemoveRuleCommand());

        registry.register(new UnionCommand());
        registry.register(new ConcatCommand());
        registry.register(new IterCommand());
        registry.register(new EmptyCommand());

        registry.register(new ChomskyCommand());
        registry.register(new ChomskifyCommand());
        registry.register(new CykCommand());

        // Създаване на контекст
        CommandContext context = new CommandContext(
                repository,
                storage,
                grammarService,
                operationsService,
                chomskyService,
                cykService,
                console,
                registry
        );

        // Стартиране на приложението
        new CommandLineEngine(
                context,
                new CommandLineParser()
        ).run();
    }
}