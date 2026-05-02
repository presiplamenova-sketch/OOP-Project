package bg.tu_varna.sit.f24621674.command.registry;


import bg.tu_varna.sit.f24621674.command.Command;
import bg.tu_varna.sit.f24621674.exception.InvalidCommandException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Регистър на всички налични команди.
 *
 * <p>Това е сърцевината, която ни позволява да нямаме <code>switch</code> или
 * огромно <code>if/else</code> за диспечиране. Работим с
 * <code>Map&lt;String, Command&gt;</code> – нови команди се регистрират с
 * <code>register()</code>, а CLI engine-ът просто пита с
 * <code>resolve(name)</code>.</p>
 *
 * <p>Регистърът е case-insensitive за имената на командите.</p>
 */
public class CommandRegistry {

    private final Map<String, Command> commands = new LinkedHashMap<>();

    /**
     * Регистрира нова команда. Ако команда с това име вече съществува – грешка.
     *
     * @param command команда за регистрация
     */
    public void register(Command command) {
        String key = command.name().toLowerCase();
        if (commands.containsKey(key)) {
            throw new IllegalStateException("Команда с име '" + command.name() + "' вече е регистрирана.");
        }
        commands.put(key, command);
    }

    /**
     * Връща команда по име, ако е регистрирана.
     *
     * @param name име на команда (case-insensitive)
     * @return Optional с командата
     */
    public Optional<Command> find(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commands.get(name.toLowerCase()));
    }

    /**
     * Връща команда или хвърля {@link InvalidCommandException}.
     *
     * @param name име на команда
     * @return командата
     */
    public Command resolve(String name) {
        return find(name).orElseThrow(() -> new InvalidCommandException(
                "Непозната команда: '" + name + "'. Използвайте 'help' за списък."));
    }

    /**
     * @return имутабилен изглед към всички регистрирани команди (подредени
     *     в реда на регистрация – удобно за help).
     */
    public Collection<Command> all() {
        return Collections.unmodifiableCollection(commands.values());
    }
}
