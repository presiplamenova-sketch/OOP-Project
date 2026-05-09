package bg.tu_varna.sit.f24621674.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Parses един ред команда въведена от потребителя
 * Разделя реда по интервали - първият токен е командата останалите са аргументи
 * Специален случай: "save as" се преобразува в "saveAs"
 */
public class CommandLineParser {

    /**
     * Съдържа резултата от четенето на един ред
     */
    public record ParsedCommand(String name, List<String> arguments) {
        /**
         * @return броят на аргументите
         */
        public int argCount() {
            return arguments.size();
        }
    }

    /**
     * Parses един ред в команда и аргументи
     * Връща null ако редът е празен
     * @param line въведеният ред
     * @return прочетената команда или null при празен вход
     */
    public ParsedCommand parse(String line) {
        if (line == null) {
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String[] parts = trimmed.split("\\s+");
        String name = parts[0];
        List<String> args = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));

        // "save as" се преобразува в "saveAs"
        if (name.equalsIgnoreCase("save") && !args.isEmpty() && args.get(0).equalsIgnoreCase("as")) {
            name = "saveAs";
            args.remove(0);
        }

        return new ParsedCommand(name, Collections.unmodifiableList(args));
    }
}