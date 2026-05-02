package bg.tu_varna.sit.f24621674.command.impl;

import bg.tu_varna.sit.f24621674.command.AbstractCommand;
import bg.tu_varna.sit.f24621674.command.CommandContext;
import bg.tu_varna.sit.f24621674.command.CommandResult;
import bg.tu_varna.sit.f24621674.model.Grammar;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Команда <code>save</code>.
 *
 * <p>Поддържа две форми:</p>
 * <ul>
 *   <li><code>save</code> (без аргументи) – записва всички заредени граматики
 *       в текущо отворения файл.</li>
 *   <li><code>save &lt;id&gt; &lt;path&gt;</code> – записва една конкретна граматика
 *       в подадения файл (не пипа текущия).</li>
 * </ul>
 *
 * <p>За запис в нов файл на цялата сесия вижте {@link SaveAsCommand}.</p>
 */
public class SaveCommand extends AbstractCommand {

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "save";
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return "Записва сесията (без аргументи) или конкретна граматика ( <id> <path> ).";
    }

    /** {@inheritDoc} */
    @Override
    public String usage() {
        return "save | save <id> <path>";
    }

    /** {@inheritDoc} */
    @Override
    public boolean requiresOpenFile() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public CommandResult execute(CommandContext context, List<String> arguments) {
        requireArgsBetween(arguments, 0, 2);

        if (arguments.isEmpty()) {
            return saveAll(context);
        }
        if (arguments.size() == 2) {
            int id = parseInt(arguments.get(0));
            Path target = Paths.get(arguments.get(1));
            Grammar g = context.getRepository().getOrThrow(id);
            context.getFileStorage().saveSingle(g, target);
            return CommandResult.ok("Записана граматика #" + id + " във файл '" + target + "'.");
        }
        return CommandResult.error("Невалидна употреба. " + usage());
    }

    /**
     * Записва всички заредени граматики в текущия файл.
     */
    private CommandResult saveAll(CommandContext context) {
        Path current = context.getCurrentFile().orElse(null);
        if (current == null) {
            return CommandResult.error("Няма отворен файл. Използвайте 'saveAs <path>'.");
        }
        List<Grammar> all = context.getRepository().all();
        context.getFileStorage().save(all, current);
        return CommandResult.ok("Записани " + all.size() + " граматики във '" + current + "'.");
    }
}
