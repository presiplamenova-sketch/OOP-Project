package bg.tu_varna.sit.f24621674.cli;

import java.util.Scanner;

public class CLI {

    private CommandProcessor processor = new CommandProcessor();

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("exit")) break;

            processor.process(input);
        }
    }
}