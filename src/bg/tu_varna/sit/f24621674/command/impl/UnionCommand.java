package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;
/**
 * Команда {@code union <id1> <id2>} - създава нова граматика чийто език е обединението на двата езика
 */
public class UnionCommand extends AbstractCommand {

    @Override
    public String name() {
        return "union";
    }

    @Override
    public String description() {
        return "Обединява два езика в нова граматика";
    }

    @Override
    public String usage() {
        return "union <id1> <id2>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 2);
        int id1 = parseInt(arguments.get(0));
        int id2 = parseInt(arguments.get(1));
        Grammar g1 = context.getRepository().getOrThrow(id1);
        Grammar g2 = context.getRepository().getOrThrow(id2);
        Grammar result = context.getOperationsService().union(g1, g2);
        context.getRepository().add(result);
        return CommandResult.ok("Създадена нова граматика #" + result.getId()
                + " = Union(#" + id1 + ", #" + id2 + ")");
    }
}