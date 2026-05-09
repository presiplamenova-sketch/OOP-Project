package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Команда saveAs <path> - записва сесията в нов файл
 * Поддържа и синтаксис save as <path>
 * Новият файл става текущ след записа
 */
public class SaveAsCommand extends AbstractCommand {

    @Override
    public String name() {
        return "saveAs";
    }

    @Override
    public String description() {
        return "Записва сесията в нов файл (поддържа и save as <path>)";
    }

    @Override
    public String usage() {
        return "saveAs <path>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 1);
        Path target = Paths.get(arguments.get(0));
        context.getFileStorage().save(context.getRepository().all(), target);
        context.setCurrentFile(target);
        return CommandResult.ok("Записани " + context.getRepository().size()
                + " граматики във '" + target + "'. Сесията вече сочи към този файл");
    }
}