package bg.tu_varna.sit.f24621674.storage;

import bg.tu_varna.sit.f24621674.exception.GrammarNotFoundException;
import bg.tu_varna.sit.f24621674.model.Grammar;
import bg.tu_varna.sit.f24621674.util.IdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранилище за всички заредени граматики в текущата сесия
 * Граматиките се пазят в паметта и се идентифицират чрез уникално ID
 */
public class GrammarRepository {

    private final Map<Integer, Grammar> grammars = new LinkedHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    /**
     * @return генераторът на ID-та
     */
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     * Добавя нова граматика в хранилището
     * Ако граматика с това ID вече съществува хвърля грешка
     * @param grammar граматиката за добавяне
     */
    public void add(Grammar grammar) {
        if (grammars.containsKey(grammar.getId())) {
            throw new IllegalStateException("Граматика с ID " + grammar.getId() + " вече съществува");
        }
        grammars.put(grammar.getId(), grammar);
    }

    /**
     * Връща граматика по ID или хвърля грешка ако не е намерена
     * @param id ID на граматиката
     * @return намерената граматика
     */
    public Grammar getOrThrow(int id) {
        Grammar g = grammars.get(id);
        if (g == null) {
            throw new GrammarNotFoundException(id);
        }
        return g;
    }

    /** @return всички граматики в реда на добавяне  */
    public List<Grammar> all() {
        return Collections.unmodifiableList(new ArrayList<>(grammars.values()));
    }

    /** Изтрива всички граматики от хранилището   */
    public void clear() {
        grammars.clear();
    }

    /** @return броят на заредените граматики   */
    public int size() {
        return grammars.size();
    }

    /**  @return true ако няма заредени граматики  */
    public boolean isEmpty() {
        return grammars.isEmpty();
    }
}