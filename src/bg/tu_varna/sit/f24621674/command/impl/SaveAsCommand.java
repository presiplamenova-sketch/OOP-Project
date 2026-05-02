package bg.tu_varna.sit.f24621674.command.impl;


import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Команда <code>saveAs &lt;path&gt;</code> (или <code>save as &lt;path&gt;</code>).
 *
 * <p>Записва всички заредени граматики в нов файл и прави този файл "текущ".</p>
 */
public class SaveAsCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "saveAs";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Записва сесията в нов файл (синтаксис 'save as <path>' също се поддържа).";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "saveAs <path>";
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
        Path target = Paths.get(arguments.get(0));
        context.getFileStorage().save(context.getRepository().all(), target);
        context.setCurrentFile(target);
        return CommandResult.ok("Записани " + context.getRepository().size()
                + " граматики във '" + target + "'. Сесията вече сочи към този файл.");
    }
}

