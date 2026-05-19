package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.model.Rule;
import bg.tu_varna.sit.f24621674.parser.RuleParser;

import java.util.List;

/**
 * Команда addRule &lt;id&gt; &lt;rule&gt; - добавя правило към граматика
 * Поддържа компактен (S->aA) и разделен (S -> a A) синтаксис
 */
public class AddRuleCommand extends AbstractCommand {

    private final RuleParser ruleParser;

    /** @param ruleParser парсер за правила   */
    public AddRuleCommand(RuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    @Override
    public String name() {
        return "addRule";
    }

    @Override
    public String description() {
        return "Добавя ново правило към граматика по ID";
    }

    @Override
    public String usage() {
        return "addRule <id> <rule>";
    }

    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgsBetween(arguments, 2, Integer.MAX_VALUE);
        int id = parseInt(arguments.get(0));
        // слепваме всичко след id за да поддържаме разделен синтаксис
        String ruleText = String.join(" ", arguments.subList(1, arguments.size()));

        Grammar g = context.getRepository().getOrThrow(id);
        Rule rule = ruleParser.parse(ruleText);
        g.addRule(rule);
        return CommandResult.ok("Добавено правило към граматика #" + id + ": " + rule);
    }
}