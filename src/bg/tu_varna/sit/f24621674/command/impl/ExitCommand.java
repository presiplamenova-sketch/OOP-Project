package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.util.List;

/**
 * Команда <code>exit</code> – заявка за прекратяване на програмата.
 *
 * <p>Не извикваме <code>System.exit()</code> директно, а казваме на
 * контекста да приключи – CLI engine ще завърши главния си цикъл коректно.</p>
 */
public class ExitCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "exit";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Излиза от програмата.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "exit";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 0);
        context.requestExit();
        return CommandResult.ok("Доскоро!");
    }
}

