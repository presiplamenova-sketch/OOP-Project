package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда cyk <id> <word> - проверява дали дума принадлежи на езика на граматика
 * Ако граматиката не е в НФЧ тя се преобразува автоматично
 * За празна дума използвайте _ или ""
 */
public class CykCommand extends AbstractCommand {

    @Override
    public String name() {
        return "cyk";
    }

    @Override
    public String description() {
        return "Проверява дали дума принадлежи на езика на граматика чрез алгоритъма CYK";
    }

    @Override
    public String usage() {
        return "cyk <id> <word>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 2);
        int id = parseInt(arguments.get(0));
        String wordRaw = arguments.get(1);
        // _ или "" означават празна дума
        String word = (wordRaw.equals("_") || wordRaw.equals("\"\"")) ? "" : wordRaw;

        Grammar g = context.getRepository().getOrThrow(id);
        boolean belongs = context.getCykService().belongs(g, word);
        return CommandResult.ok("Думата '" + word + "' "
                + (belongs ? "ПРИНАДЛЕЖИ" : "НЕ ПРИНАДЛЕЖИ")
                + " на езика на граматика #" + id);
    }
}