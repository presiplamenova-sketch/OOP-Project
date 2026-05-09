package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.util.List;

/**
 * Команда exit - излиза от програмата
 * Не извиква System.exit() директно а маркира контекста за излизане
 * така че главният цикъл приключва коректно
 */
public class ExitCommand extends AbstractCommand {

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public String description() {
        return "Излиза от програмата";
    }

    @Override
    public String usage() {
        return "exit";
    }

    @Override
    public boolean requiresOpenFile() {
        return false;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 0);
        context.requestExit();
        return CommandResult.ok("Изход от програмата!");
    }
}