package bg.tu_varna.sit.f24621674.command.impl;


import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>chomskify &lt;id&gt;</code> – преобразува граматика до НФЧ
 * и добавя резултата в репозиторито като нова граматика.
 */
public class ChomskifyCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "chomskify";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Преобразува граматика до нормална форма на Чомски.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "chomskify <id>";
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
        Grammar cnf = context.getChomskyService().toChomskyNormalForm(g);
        context.getRepository().add(cnf);
        return CommandResult.ok("Създадена нова граматика #" + cnf.getId()
                + " = CNF(#" + id + "). Използвайте 'print " + cnf.getId() + "' за преглед.");
    }
}
