package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.Rule;

import java.util.List;

/**
 * Команда <code>removeRule &lt;id&gt; &lt;ruleNumber&gt;</code> –
 * премахва правило по неговия номер (1-базиран, такъв какъвто го показва <code>print</code>).
 */
public class RemoveRuleCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "removeRule";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Премахва правило от граматика по неговия номер.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "removeRule <id> <ruleNumber>";
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
        int ruleNumber = parseInt(arguments.get(1));
        Grammar g = context.getRepository().getOrThrow(id);
        Rule removed = g.removeRule(ruleNumber);
        return CommandResult.ok("Премахнато правило #" + ruleNumber + " от граматика #" + id + ": " + removed);
    }
}
