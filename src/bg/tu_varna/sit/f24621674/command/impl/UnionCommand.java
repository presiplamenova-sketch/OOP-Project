package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>union &lt;id1&gt; &lt;id2&gt;</code> – създава нова граматика,
 * чийто език е обединението на двата.
 */
public class UnionCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "union";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Обединява два езика в нова граматика.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "union <id1> <id2>";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    /** {@inheritDoc} */
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
                + " = Union(#" + id1 + ", #" + id2 + ").");
    }
}
