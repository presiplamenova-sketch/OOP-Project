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
 * Главен цикъл на командния интерфейс
 * Чете команди от конзолата, изпълнява ги и показва резултата
 * Спира когато потребителят напише exit или затвори входния поток
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
     * Стартира главния цикъл
     * Спира само при exit или край на входния поток
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
     * специално за да може да се ползва от unit тестове
     * @param line ред въведен от потребителя
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

    /** Показва съобщение при стартиране   */
    private void printWelcome() {
        console.println("  CFG TOOL – Контекстно-свободни граматики");
        console.println("  Напишете 'help' за списък с команди.");
    }

    /**
     * Показва текущото състояние в промпта
     * Ако има отворен файл показва неговото име
     */
    private String prompt() {
        if (context.isFileOpen()) {
            return "cfg(" + context.getCurrentFile().get().getFileName() + ")> ";
        }
        return "cfg> ";
    }
}
