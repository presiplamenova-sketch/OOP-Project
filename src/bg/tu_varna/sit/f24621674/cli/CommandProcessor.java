package bg.tu_varna.sit.f24621674.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {

    private Map<String, Command> commands = new HashMap<>();

    public CommandProcessor() {
        commands.put("list", new bg.tu_varna.sit.f24621674.cli.commands.ListCommand());

    }

    public void process(String input) {
        String[] parts = input.split("\\s+");

        Command command = commands.get(parts[0]);

        if (command == null) {
            System.out.println("Unknown command");
            return;
        }

        command.execute(parts);
    }
}