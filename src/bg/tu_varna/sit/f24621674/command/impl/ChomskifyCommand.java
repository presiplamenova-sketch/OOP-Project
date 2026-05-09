package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда chomskify <id> - преобразува граматика до нормална форма на Чомски
 * Създава нова граматика и я добавя в архив
 */
public class ChomskifyCommand extends AbstractCommand {

    @Override
    public String name() {
        return "chomskify";
    }

    @Override
    public String description() {
        return "Преобразува граматика до нормална форма на Чомски";
    }

    @Override
    public String usage() {
        return "chomskify <id>";
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
        Grammar cnf = context.getChomskyService().toChomskyNormalForm(g);
        context.getRepository().add(cnf);
        return CommandResult.ok("Създадена нова граматика #" + cnf.getId()
                + " = CNF(#" + id + "). Използвайте 'print " + cnf.getId() + "' за преглед");
    }
}