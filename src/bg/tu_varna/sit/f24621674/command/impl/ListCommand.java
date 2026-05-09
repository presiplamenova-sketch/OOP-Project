package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.util.List;

/**
 * Команда list - показва всички заредени граматики и техните ID-та
 */
public class ListCommand extends AbstractCommand {

    @Override
    public String name() {
        return "list";
    }

    @Override
    public String description() {
        return "Показва всички заредени граматики и техните ID-та";
    }

    @Override
    public String usage() {
        return "list";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgs(arguments, 0);
        List<Grammar> all = context.getRepository().all();
        if (all.isEmpty()) {
            return CommandResult.ok("Няма заредени граматики");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Заредени граматики (общо ").append(all.size()).append("):\n");
        for (Grammar g : all) {
            sb.append("  ").append(context.getGrammarService().formatForList(g)).append('\n');
        }
        return CommandResult.ok(sb.toString());
    }
}