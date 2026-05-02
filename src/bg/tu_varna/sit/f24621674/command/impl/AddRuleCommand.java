package bg.tu_varna.sit.f24621674.command.impl;


import  bg.tu_varna.sit.f24621674.command.AbstractCommand;
import  bg.tu_varna.sit.f24621674.command.CommandContext;
import  bg.tu_varna.sit.f24621674.command.CommandResult;
import  bg.tu_varna.sit.f24621674.model.Grammar;
import  bg.tu_varna.sit.f24621674.model.Rule;
import  bg.tu_varna.sit.f24621674.parser.RuleParser;

import java.util.List;

/**
 * Команда <code>addRule &lt;id&gt; &lt;rule&gt;</code> – добавя правило към граматика.
 *
 * <p>Поддържа както компактен (<code>S-&gt;aA</code>), така и spaced
 * (<code>S -&gt; a A</code>) синтаксис. Тъй като парсерът ни може да получи
 * няколко токена, ако потребителят е написал <code>addRule 1 S -&gt; a A</code>,
 * слепваме всички аргументи след първия в един низ.</p>
 */
public class AddRuleCommand extends AbstractCommand {

    private final RuleParser ruleParser;

    /**
     * @param ruleParser парсер за правила (инжектиран централно от bootstrap)
     */
    public AddRuleCommand(RuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "addRule";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Добавя ново правило към граматика по ID.";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "addRule <id> <rule>";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgsBetween(arguments, 2, Integer.MAX_VALUE);
        int id = parseInt(arguments.get(0));
        // слепваме всичко след id, за да поддържаме spaced синтаксис
        String ruleText = String.join(" ", arguments.subList(1, arguments.size()));

        Grammar g = context.getRepository().getOrThrow(id);
        Rule rule = ruleParser.parse(ruleText);
        g.addRule(rule);
        return CommandResult.ok("Добавено правило към граматика #" + id + ": " + rule);
    }
}
