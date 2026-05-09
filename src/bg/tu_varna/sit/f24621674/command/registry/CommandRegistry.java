package bg.tu_varna.sit.f24621674.command.registry;

import bg.tu_varna.sit.f24621674.command.Command;
import bg.tu_varna.sit.f24621674.exception.InvalidCommandException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Регистър на всички налични команди
 * Позволява регистрация и търсене на команди по име
 * Имената на командите не са чувствителни към главни и малки букви
 */
public class CommandRegistry {

    private final Map<String, Command> commands = new LinkedHashMap<>();

    /**
     * Регистрира нова команда
     * Ако команда с това име вече съществува хвърля грешка
     * @param command команда за регистрация
     */
    public void register(Command command) {
        String key = command.name().toLowerCase();
        if (commands.containsKey(key)) {
            throw new IllegalStateException("Команда с име '" + command.name() + "' вече е регистрирана");
        }
        commands.put(key, command);
    }

    /**
     * Търси команда по име
     * @param name име на командата
     * @return Optional с командата ако е намерена
     */
    public Optional<Command> find(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commands.get(name.toLowerCase()));
    }

    /**
     * Връща команда по име или хвърля грешка ако не е намерена
     * @param name име на командата
     * @return намерената команда
     */
    public Command resolve(String name) {
        return find(name).orElseThrow(() -> new InvalidCommandException(
                "Непозната команда: '" + name + "'. Използвайте 'help' за списък"));
    }

    /** Връща всички регистрирани команди в реда на регистрация  */
    public Collection<Command> all() {
        return Collections.unmodifiableCollection(commands.values());
    }
}