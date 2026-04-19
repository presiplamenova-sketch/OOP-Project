package bg.tu_varna.sit.f24621674.cli.commands;

import bg.tu_varna.sit.f24621674.cli.Command;
import bg.tu_varna.sit.f24621674.storage.GrammarRepository;

public class ListCommand implements Command {

    private GrammarRepository repository;

    public ListCommand(GrammarRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        repository.getAll().forEach((k,v) -> System.out.println(k));
    }
}