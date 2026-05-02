package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>empty &lt;id&gt;</code> – проверява дали езикът на граматика е празен.
 */
public class EmptyCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "empty";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Проверява дали езикът на граматика е празен.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "empty <id>";
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
        boolean empty = context.getOperationsService().isLanguageEmpty(g);
        return CommandResult.ok("Езикът на граматика #" + id + (empty ? " Е празен." : " НЕ Е празен."));
    }
}
