package bg.tu_varna.sit.f24621674.command;

import java.util.List;

/**
 * Интерфейс за всяка команда която потребителят може да изпълни
 * Всяка нова команда трябва да имплементира този интерфейс
 */
public interface Command {

    /**
     * Уникалното име на командата
     * @return името на командата
     */
    String name();

    /**
     * Кратко описание на командата което се вижда при help
     * @return описанието на командата
     */
    String description();

    /**
     * Синтаксисът на командата който се вижда при help
     * @return синтаксиса на командата
     */
    String usage();

    /**
     * Указва дали командата изисква отворен файл
     * @return true ако командата изисква отворен файл
     */
    boolean requiresOpenFile();

    /**
     * Изпълнява командата
     * @param context контекстът на приложението
     * @param arguments аргументите подадени от потребителя
     * @return резултат от изпълнението
     */
    CommandResult execute(CommandContext context, List<String> arguments);
}