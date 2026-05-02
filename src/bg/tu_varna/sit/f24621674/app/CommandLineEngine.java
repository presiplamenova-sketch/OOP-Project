package bg.tu_varna.sit.f24621674.app;

import  bg.tu_varna.sit.f24621674.command.Command;
import  bg.tu_varna.sit.f24621674.command.CommandContext;
import  bg.tu_varna.sit.f24621674.command.CommandResult;
import  bg.tu_varna.sit.f24621674.exception.CfgException;
import  bg.tu_varna.sit.f24621674.exception.InvalidCommandException;
import  bg.tu_varna.sit.f24621674.parser.CommandLineParser;
import  bg.tu_varna.sit.f24621674.util.ConsoleIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Главният цикъл на командния интерфейс.
 *
 * <p>Чете ред по ред от стандартния вход, парси чрез {@link CommandLineParser},
 * резолва командата от регистъра в {@link CommandContext} и я изпълнява.
 * Резултатът се извежда чрез {@link ConsoleIO}.</p>
 *
 * <p>Цикълът приключва когато потребителят зададе exit или входният поток
 * приключи (Ctrl+D / EOF).</p>
 */
public class CommandLineEngine {

    private final CommandContext context;
    private final CommandLineParser parser;
    private final ConsoleIO console;

    /**
     * @param context предварително изграден контекст с всички services и регистър
     * @param parser парсер за команден ред
     */
    public CommandLineEngine(CommandContext context, CommandLineParser parser) {
        this.context = context;
        this.parser = parser;
        this.console = context.getConsole();
    }

    /**
     * Стартира главния цикъл. Връща се само при exit.
     */
    public void run() {
        printWelcome();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            while (!context.isExitRequested()) {
                console.print(prompt());
                String line = reader.readLine();
                if (line == null) {
                    // EOF – излизаме чисто
                    console.println("");
                    break;
                }
                executeLine(line);
            }
        } catch (IOException e) {
            console.error("Прекъснат вход: " + e.getMessage());
        }
    }

    /**
     * Изпълнява една команда от текстов ред. Изнесено в отделен метод
     * специално за да може да се ползва от unit тестове.
     *
     * @param line ред от потребителя
     */
    public void executeLine(String line) {
        CommandLineParser.ParsedCommand parsed = parser.parse(line);
        if (parsed == null) {
            return;
        }
        try {
            Command command = context.getCommandRegistry().resolve(parsed.name());
            if (command.requiresOpenFile() && !context.isFileOpen()) {
                throw new InvalidCommandException(
                        "Командата '" + command.name() + "' изисква първо да отворите файл (open).");
            }
            CommandResult result = command.execute(context, parsed.arguments());
            if (result.isSuccess()) {
                if (!result.getMessage().isEmpty()) {
                    console.println(result.getMessage());
                }
            } else {
                console.error(result.getMessage());
            }
        } catch (CfgException e) {
            console.error(e.getMessage());
        } catch (RuntimeException e) {
            console.error("Неочаквана грешка: " + e.getMessage());
        }
    }

    /**
     * Извежда поздрав и кратки инструкции в началото.
     */
    private void printWelcome() {
        console.println("  CFG TOOL – Контекстно-свободни граматики");
        console.println("  Напишете 'help' за списък с команди.");
    }

    /**
     * Промптът показва дали има отворен файл – подобрява UX-а.
     */
    private String prompt() {
        if (context.isFileOpen()) {
            return "cfg(" + context.getCurrentFile().get().getFileName() + ")> ";
        }
        return "cfg> ";
    }
}
