package bg.tu_varna.sit.f24621674.command;


import java.util.List;

/**
 * Общ интерфейс за всяка команда, която потребителят може да изпълни.
 *
 * <p>Това е сърцето на Command pattern-а в проекта
 * работи само с този интерфейс и ни най-малко не знае какви конкретно команди има.
 * Добавянето на нова команда = нов клас имплементиращ <code>Command</code> + запис в
 * {@link bg.tu_varna.sit.f24621674.command}.</p>
 */
public interface Command {

    /**
     * Уникалното текстово име на командата (напр. "open", "print", "addRule").
     * Това е ключът в регистъра.
     *
     * @return име
     */
    String name();

    /**
     * Кратко еднорядково описание – използва се от {@code help}.
     *
     * @return описание
     */
    String description();

    /**
     * Показва какъв е очакваният синтаксис на командата – отново за help.
     *
     * @return синтаксис, напр. "print &lt;id&gt;"
     */
    String usage();

    /**
     * Указва дали командата изисква вече отворен файл (т.е. активна сесия).
     *
     * @return true ако командата не може да се изпълни преди open
     */
    boolean requiresOpenFile();

    /**
     * Изпълнява командата.
     *
     * @param context контекстът на приложението (репозито, текущ файл и т.н.)
     * @param arguments аргументите, подадени от потребителя
     * @return резултат от изпълнението
     */
    CommandResult execute(CommandContext context, List<String> arguments);
}

