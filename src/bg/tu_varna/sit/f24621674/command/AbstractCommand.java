package bg.tu_varna.sit.f24621674.command;

import bg.tu_varna.sit.f24621674.exception.InvalidCommandException;

import java.util.List;

/**
 * Базов клас, който носи помощни методи за валидация на аргументи.
 *
 * <p>Конкретните команди разширяват този клас, за да не дублират проверки от вида
 * "точно колко аргумента трябва да има". Запазваме гъвкавост – класът има само
 * полезни методи; не налага template method pattern, който да обърква.</p>
 */
public abstract class AbstractCommand implements Command {

    /**
     * Проверява, че броят на подадените аргументи е точно очакваният.
     * @param args подадени аргументи
     * @param expected очакван брой
     * @throws InvalidCommandException ако броят не съвпада
     */
    protected void requireArgs(List<String> args, int expected) {
        if (args.size() != expected) {
            throw new InvalidCommandException(
                    "Командата '" + name() + "' очаква точно " + expected + " аргумент(а). "
                            + "Употреба: " + usage());
        }
    }

    /**
     * Проверява, че броят на аргументите е в зададения интервал [min..max].
     */
    protected void requireArgsBetween(List<String> args, int min, int max) {
        if (args.size() < min || args.size() > max) {
            throw new InvalidCommandException(
                    "Командата '" + name() + "' очаква между " + min + " и " + max
                            + " аргумента. Употреба: " + usage());
        }
    }

    /**
     * Парсва аргумент като цяло число – с разбираема грешка ако не е такова.
     * @param raw низ за парсване
     * @return цяло число
     */
    protected int parseInt(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("'" + raw + "' не е валидно цяло число.");
        }
    }
}
