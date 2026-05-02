package bg.tu_varna.sit.f24621674.command.impl;


import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>concat &lt;id1&gt; &lt;id2&gt;</code> – създава нова граматика
 * чийто език е конкатенацията L(g1)·L(g2).
 */
public class ConcatCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "concat";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Конкатенира езиците на две граматики в нова.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "concat <id1> <id2>";
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
        Grammar result = context.getOperationsService().concat(g1, g2);
        context.getRepository().add(result);
        return CommandResult.ok("Създадена нова граматика #" + result.getId()
                + " = Concat(#" + id1 + ", #" + id2 + ").");
    }
}
