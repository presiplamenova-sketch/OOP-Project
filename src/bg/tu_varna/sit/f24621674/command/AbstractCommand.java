package bg.tu_varna.sit.f24621674.command;

import bg.tu_varna.sit.f24621674.exception.InvalidCommandException;

import java.util.List;

/**
 * Базов клас с помощни методи за валидация на аргументи
 * Всички команди наследяват този клас за да не повтарят
 * едни и същи проверки
 */
public abstract class AbstractCommand implements Command {

    /**
     * Проверява че броят на аргументите е точно очакваният
     * @param args подадени аргументи
     * @param expected очакван брой
     */
    protected void requireArgs(List<String> args, int expected) {
        if (args.size() != expected) {
            throw new InvalidCommandException(
                    "Командата '" + name() + "' очаква точно " + expected + " аргумент(а). "
                            + "Употреба: " + usage());
        }
    }

    /**
     * Проверява че броят на аргументите е в интервала [min..max]
     * @param args подадени аргументи
     * @param min минимален брой
     * @param max максимален брой
     */
    protected void requireArgsBetween(List<String> args, int min, int max) {
        if (args.size() < min || args.size() > max) {
            throw new InvalidCommandException(
                    "Командата '" + name() + "' очаква между " + min + " и " + max
                            + " аргумента. Употреба: " + usage());
        }
    }

    /**
     * Преобразува низ в цяло число
     * Хвърля грешка ако низът не е валидно число
     * @param raw низ за преобразуване
     * @return цялото число
     */
    protected int parseInt(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("'" + raw + "' не е валидно цяло число");
        }
    }
}