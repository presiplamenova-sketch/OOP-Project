package bg.tu_varna.sit.f24621674.storage;

import bg.tu_varna.sit.f24621674.exception.StorageException;
import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.parser.GrammarFileParser;
import bg.tu_varna.sit.f24621674.util.IdGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Слой за четене и запис на граматики от и във файлове
 * Всички грешки при работа с файлове се увиват в StorageException
 */
public class FileGrammarStorage {

    private final GrammarFileParser fileParser;

    /**
     * @param fileParser четецът за граматики който се използва вътрешно
     */
    public FileGrammarStorage(GrammarFileParser fileParser) {
        this.fileParser = fileParser;
    }

    /**
     * Зарежда всички граматики от файл
     * @param path пътят до файла
     * @param idGenerator генератор за ID-та на новите граматики
     * @return списък със заредените граматики
     */
    public List<Grammar> load(Path path, IdGenerator idGenerator) {
        try {
            if (!Files.exists(path)) {
                throw new StorageException("Файлът '" + path + "' не съществува");
            }
            String content = Files.readString(path);
            return fileParser.parseAll(content, idGenerator);
        } catch (IOException e) {
            throw new StorageException("Не може да се прочете файл '" + path + "'", e);
        }
    }

    /**
     * Записва граматики във файл
     * @param grammars списък граматики за запис
     * @param path целевият файл
     */
    public void save(List<Grammar> grammars, Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            String content = fileParser.serializeAll(grammars);
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new StorageException("Не може да се запише файл '" + path + "'", e);
        }
    }

    /**
     * Записва една граматика в отделен файл
     * @param grammar граматиката за запис
     * @param path пътят до изходния файл
     */
    public void saveSingle(Grammar grammar, Path path) {
        save(List.of(grammar), path);
    }
}