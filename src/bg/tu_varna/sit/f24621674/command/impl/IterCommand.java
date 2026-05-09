package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда iter <id> - създава нова граматика за звездата на Клини
 */
public class IterCommand extends AbstractCommand {

    @Override
    public String name() {
        return "iter";
    }

    @Override
    public String description() {
        return "Създава нова граматика за звездата на Клини на езика";
    }

    @Override
    public String usage() {
        return "iter <id>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 1);
        int id = parseInt(arguments.get(0));
        Grammar g = context.getRepository().getOrThrow(id);
        Grammar result = context.getOperationsService().iter(g);
        context.getRepository().add(result);
        return CommandResult.ok("Създадена нова граматика #" + result.getId()
                + " = Iter(#" + id + ")");
    }
}