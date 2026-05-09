package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.Command;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.util.Collection;
import java.util.List;

/**
 * Команда help - показва списък с всички налични команди
 * Информацията се взима директно от регистъра на командите така че списъкът винаги е актуален
 */
public class HelpCommand extends AbstractCommand {

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "Показва списък с всички налични команди";
    }

    @Override
    public String usage() {
        return "help";
    }

    @Override
    public boolean requiresOpenFile() {
        return false;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        Collection<Command> commands = context.getCommandRegistry().all();
        StringBuilder sb = new StringBuilder();
        sb.append("Налични команди:\n");
        for (Command c : commands) {
            sb.append("  ").append(c.usage()).append(" - ").append(c.description()).append("\n");
        }
        sb.append("\nПодсказка: команди като 'addRule', 'cyk', 'union' изискват отворен файл");
        return CommandResult.ok(sb.toString());
    }
}