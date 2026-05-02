package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.util.List;

/**
 * Команда <code>close</code> – затваря текущия файл и изчиства заредените граматики.
 */
public class CloseCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "close";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Затваря текущия файл и изчиства заредените граматики.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "close";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 0);
        String file = context.getCurrentFile().map(Object::toString).orElse("(без име)");
        context.getRepository().clear();
        context.clearCurrentFile();
        return CommandResult.ok("Затворен файл '" + file + "'. Граматиките са изчистени.");
    }
}
