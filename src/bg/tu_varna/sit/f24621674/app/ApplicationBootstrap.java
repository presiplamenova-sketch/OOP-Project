package bg.tu_varna.sit.f24621674.app;

import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.impl.AddRuleCommand;
import bg.tu_varna.sit.f24621674.command.impl.ChomskifyCommand;
import bg.tu_varna.sit.f24621674.command.impl.ChomskyCommand;
import bg.tu_varna.sit.f24621674.command.impl.CloseCommand;
import bg.tu_varna.sit.f24621674.command.impl.ConcatCommand;
import bg.tu_varna.sit.f24621674.command.impl.CykCommand;
import bg.tu_varna.sit.f24621674.command.impl.EmptyCommand;
import bg.tu_varna.sit.f24621674.command.impl.ExitCommand;
import bg.tu_varna.sit.f24621674.command.impl.HelpCommand;
import bg.tu_varna.sit.f24621674.command.impl.IterCommand;
import bg.tu_varna.sit.f24621674.command.impl.ListCommand;
import bg.tu_varna.sit.f24621674.command.impl.OpenCommand;
import bg.tu_varna.sit.f24621674.command.impl.PrintCommand;
import bg.tu_varna.sit.f24621674.command.impl.RemoveRuleCommand;
import bg.tu_varna.sit.f24621674.command.impl.SaveAsCommand;
import bg.tu_varna.sit.f24621674.command.impl.SaveCommand;
import bg.tu_varna.sit.f24621674.command.impl.UnionCommand;
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
 * Композиционен корен на приложението.
 *
 * <p>Тук се сглобяват всички зависимости – тип DI контейнер на ръчно.
 * Класът има само една публична задача: да върне напълно подготвен
 * {@link CommandLineEngine}. Така <code>Main</code> остава елементарен.</p>
 *
 * <p>Регистрираме всички команди централно тук – това е <b>единственото</b>
 * място, което знае за конкретните команди. Парсерът, engine-ът и контекстът
 * работят само през абстракции.</p>
 */
public class ApplicationBootstrap {

    /**
     * Изгражда напълно конфигуриран {@link CommandLineEngine}.
     *
     * @return готов CLI engine
     */
    public CommandLineEngine bootstrap() {
        // Базови инструменти
        ConsoleIO console = new ConsoleIO();
        RuleParser ruleParser = new RuleParser();
        GrammarFileParser grammarFileParser = new GrammarFileParser(ruleParser);

        // Хранилище и storage
        GrammarRepository repository = new GrammarRepository();
        FileGrammarStorage storage = new FileGrammarStorage(grammarFileParser);

        // Сервизи
        GrammarService grammarService = new GrammarService();
        GrammarOperationsService operationsService =
                new GrammarOperationsService(repository.getIdGenerator());
        ChomskyService chomskyService = new ChomskyService(repository.getIdGenerator());
        CykService cykService = new CykService(chomskyService);

        // Регистрираме командите
        CommandRegistry registry = new CommandRegistry();
        registerCommands(registry, ruleParser);

        // Контекст и engine
        CommandContext context = new CommandContext(
                repository, storage, grammarService, operationsService,
                chomskyService, cykService, console, registry);

        return new CommandLineEngine(context, new CommandLineParser());
    }

    /**
     * Регистрира всички команди в реда, в който искаме да се появят в help.
     */
    private void registerCommands(CommandRegistry registry, RuleParser ruleParser) {
        // общи
        registry.register(new HelpCommand());
        registry.register(new OpenCommand());
        registry.register(new CloseCommand());
        registry.register(new SaveCommand());
        registry.register(new SaveAsCommand());
        registry.register(new ExitCommand());

        // специализирани
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
    }
}
