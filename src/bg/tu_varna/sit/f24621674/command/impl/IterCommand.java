package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>iter &lt;id&gt;</code> – генерира звезда на Клини на даден език.
 */
public class IterCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "iter";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Генерира нова граматика за итерацията (звезда на Клини) на езика.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "iter <id>";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 1);
        int id = parseInt(arguments.get(0));
        Grammar g = context.getRepository().getOrThrow(id);
        Grammar result = context.getOperationsService().iter(g);
        context.getRepository().add(result);
        return CommandResult.ok("Създадена нова граматика #" + result.getId()
                + " = Iter(#" + id + ").");
    }
}
