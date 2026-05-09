package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/** Команда chomsky <id> - проверява дали граматика е в нормална форма на Чомски */
public class ChomskyCommand extends AbstractCommand {

    @Override
    public String name() {
        return "chomsky";
    }

    @Override
    public String description() {
        return "Проверява дали граматика е в нормална форма на Чомски (НФЧ).";
    }

    @Override
    public String usage() {
        return "chomsky <id>";
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
        boolean inCnf = context.getChomskyService().isInChomskyNormalForm(g);
        return CommandResult.ok("Граматика #" + id + (inCnf ? " Е в НФЧ." : " НЕ Е в НФЧ."));
    }
}
