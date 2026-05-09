package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Команда save - записва направените промени
 * Без аргументи записва всички граматики в текущия файл
 * С аргументи save <id> <path> записва една граматика в нов файл
 */
public class SaveCommand extends AbstractCommand {

    @Override
    public String name() {
        return "save";
    }

    @Override
    public String description() {
        return "Записва сесията или конкретна граматика с save <id> <path>";
    }

    @Override
    public String usage() {
        return "save | save <id> <path>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgsBetween(arguments, 0, 2);

        if (arguments.isEmpty()) {
            return saveAll(context);
        }
        if (arguments.size() == 2) {
            int id = parseInt(arguments.get(0));
            Path target = Paths.get(arguments.get(1));
            Grammar g = context.getRepository().getOrThrow(id);
            context.getFileStorage().saveSingle(g, target);
            return CommandResult.ok("Записана граматика #" + id + " във файл '" + target + "'");
        }
        return CommandResult.error("Невалидна употреба. " + usage());
    }

    /**
     * Записва всички граматики в текущия файл
     */
    private CommandResult saveAll(CommandContext context) {
        Path current = context.getCurrentFile().orElse(null);
        if (current == null) {
            return CommandResult.error("Няма отворен файл. Използвайте 'saveAs <path>'");
        }
        List<Grammar> all = context.getRepository().all();
        context.getFileStorage().save(all, current);
        return CommandResult.ok("Записани " + all.size() + " граматики във '" + current + "'");
    }
}