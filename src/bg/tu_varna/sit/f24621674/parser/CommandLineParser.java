package bg.tu_varna.sit.f24621674.parser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Парсер за един ред команда от потребителя.
 *
 * <p>Принципът е прост: разделяме реда по интервали. Първият токен е името
 * на командата, останалите – нейните аргументи.</p>
 *
 * <p>Има едно специално правило: ако потребителят е написал
 * <code>save as &lt;path&gt;</code> (две думи), сливаме ги в <code>saveAs</code>.</p>
 */
public class CommandLineParser {

    /**
     * Носи резултата от парсването на един ред.
     */
    public record ParsedCommand(String name, List<String> arguments) {
        /**
         * Връща броя аргументи.
         * @return broй аргументи
         */
        public int argCount() {
            return arguments.size();
        }
    }

    /**
     * Парсва един ред в команда + аргументи. Връща <code>null</code> ако
     * редът е празен или състои се само от whitespace.
     *
     * @param line въведеният ред
     * @return парснатата команда или null при празен вход
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

        // специален случай: "save as ..." -> "saveAs ..."
        if (name.equalsIgnoreCase("save") && !args.isEmpty() && args.get(0).equalsIgnoreCase("as")) {
            name = "saveAs";
            args.remove(0);
        }

        return new ParsedCommand(name, Collections.unmodifiableList(args));
    }
}
