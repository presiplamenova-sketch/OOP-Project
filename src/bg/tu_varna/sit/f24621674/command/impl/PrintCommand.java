package bg.tu_varna.sit.f24621674.command.impl;


import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>print &lt;id&gt;</code> – отпечатва в подробен вид конкретна граматика.
 *
 * <p>Правилата се извеждат с номера (от 1 нагоре), защото те се използват
 * от {@link RemoveRuleCommand}.</p>
 */
public class PrintCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "print";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Отпечатва граматика с номерирани правила.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "print <id>";
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
        return CommandResult.ok(context.getGrammarService().formatForPrint(g));
    }
}
