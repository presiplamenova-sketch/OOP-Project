package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда print &lt;id&gt; - показва граматика с номерирани правила
 * Номерата на правилата се използват от командата removeRule
 */

public class PrintCommand extends AbstractCommand {

    @Override
    public String name() {
        return "print";
    }

    @Override
    public String description() {
        return "Показва граматика с номерирани правила";
    }

    @Override
    public String usage() {
        return "print <id>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 1);
        int id = parseInt(arguments.get(0));
        Grammar g = context.getRepository().getOrThrow(id);
        return CommandResult.ok(context.getGrammarService().formatForPrint(g));
    }
}