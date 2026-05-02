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
 * Слой за четене и запис на граматики във/от файлове.
 *
 * <p>Адаптер между {@link GrammarFileParser} (който работи с низове)
 * и реалния файлов вход/изход. Всички I/O грешки се увиват в
 * {@link StorageException} със смислено съобщение на български.</p>
 */
public class FileGrammarStorage {

    private final GrammarFileParser fileParser;

    /**
     * @param fileParser парсерът, с който ще се четат и пишат граматиките
     */
    public FileGrammarStorage(GrammarFileParser fileParser) {
        this.fileParser = fileParser;
    }

    /**
     * Зарежда всички граматики от файл.
     *
     * @param path пътя до файла
     * @param idGenerator генератор за ID-та на новите граматики
     * @return списък заредени граматики
     * @throws StorageException при I/O грешки
     */
    public List<Grammar> load(Path path, IdGenerator idGenerator) {
        try {
            if (!Files.exists(path)) {
                throw new StorageException("Файлът '" + path + "' не съществува.");
            }
            String content = Files.readString(path);
            return fileParser.parseAll(content, idGenerator);
        } catch (IOException e) {
            throw new StorageException("Не може да се прочете файл '" + path + "'.", e);
        }
    }

    /**
     * Записва подадените граматики във файл, като ги сериализира с текущия парсер.
     *
     * @param grammars списък граматики за запис
     * @param path целевият файл
     * @throws StorageException при I/O грешки
     */
    public void save(List<Grammar> grammars, Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            String content = fileParser.serializeAll(grammars);
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new StorageException("Не може да се запише файл '" + path + "'.", e);
        }
    }

    /**
     * Удобен метод за запис на една граматика в собствен файл.
     *
     * @param grammar граматиката
     * @param path пътя до изходния файл
     */
    public void saveSingle(Grammar grammar, Path path) {
        save(List.of(grammar), path);
    }
}
