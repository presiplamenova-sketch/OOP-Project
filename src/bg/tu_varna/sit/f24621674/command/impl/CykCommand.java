package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда <code>cyk &lt;id&gt; &lt;word&gt;</code> – проверява дали даден низ принадлежи
 * на езика на граматика, използвайки CYK алгоритъма.
 *
 * <p>Ако граматиката не е в НФЧ, тя се преобразува автоматично преди старта
 * на алгоритъма (без да се мутира оригиналната). За проверка на принадлежност
 * на празната дума използвайте <code>cyk &lt;id&gt; ""</code> или специалния
 * символ <code>_</code>.</p>
 */
public class CykCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "cyk";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Проверява дали дума принадлежи на езика на граматика (алгоритъм CYK).";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "cyk <id> <word>";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 2);
        int id = parseInt(arguments.get(0));
        String wordRaw = arguments.get(1);
        // позволяваме "_" или "\"\"" като означение за празна дума
        String word = (wordRaw.equals("_") || wordRaw.equals("\"\"")) ? "" : wordRaw;

        Grammar g = context.getRepository().getOrThrow(id);
        boolean belongs = context.getCykService().belongs(g, word);
        return CommandResult.ok("Думата '" + word + "' "
                + (belongs ? "ПРИНАДЛЕЖИ" : "НЕ ПРИНАДЛЕЖИ")
                + " на езика на граматика #" + id + ".");
    }
}
