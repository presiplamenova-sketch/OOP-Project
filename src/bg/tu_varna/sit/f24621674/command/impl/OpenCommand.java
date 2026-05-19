package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Команда open &lt;path&gt; - отваря файл с граматики
 * Ако файлът съществува зарежда граматиките от него
 * Ако не съществува създава празна сесия свързана с този път
 */
public class OpenCommand extends AbstractCommand {

    @Override
    public String name() {
        return "open";
    }

    @Override
    public String description() {
        return "Отваря файл с граматики и ги зарежда в паметта";
    }

    @Override
    public String usage() {
        return "open <path>";
    }

    @Override
    public boolean requiresOpenFile() {
        return false;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 1);
        Path path = Paths.get(arguments.get(0));

        if (context.isFileOpen()) {
            return CommandResult.error(
                    "Вече има отворен файл (" + context.getCurrentFile().orElseThrow()  + "). Изпълнете 'close' преди да отворите друг");
        }

        if (!Files.exists(path)) {
            // файлът не съществува - създаваме празна сесия
            context.setCurrentFile(path);
            return CommandResult.ok("Файлът не съществува. Създадена е празна сесия за '" + path + "'");
        }

        List<Grammar> loaded = context.getFileStorage()
                .load(path, context.getRepository().getIdGenerator());
        for (Grammar g : loaded) {
            context.getRepository().add(g);
        }
        context.setCurrentFile(path);
        return CommandResult.ok("Отворен файл '" + path + "'. Заредени " + loaded.size() + " граматики");
    }
}