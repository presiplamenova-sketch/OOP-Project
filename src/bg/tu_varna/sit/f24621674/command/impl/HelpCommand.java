package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.Command;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.util.Collection;
import java.util.List;

/**
 * Команда <code>help</code> – извежда списък с всички налични команди и
 * техния синтаксис.
 *
 * <p>Командата ползва регистъра, за да открие всички останали – така остава
 * винаги в синхрон, дори когато добавим нова команда.</p>
 */
public class HelpCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "help";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Извежда списък на всички команди.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "help";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        Collection<Command> commands = context.getCommandRegistry().all();
        StringBuilder sb = new StringBuilder();
        sb.append("Налични команди:\n");
        for (Command c : commands) {
            sb.append(String.format("  %-25s - %s%n", c.usage(), c.description()));
        }
        sb.append("\nПодсказка: команди като 'addRule', 'cyk', 'union' изискват вече отворен файл.");
        return CommandResult.ok(sb.toString());
    }
}
