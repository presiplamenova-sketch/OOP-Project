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
 * In-memory хранилище за всички заредени граматики през текущата сесия.
 *
 * <p>Всяка граматика се идентифицира чрез цяло уникално <code>id</code>.
 * Поддържаме реда на добавяне (LinkedHashMap), за да може <code>list</code>
 * да показва граматиките в смислен ред.</p>
 *
 * <p>Тук е и {@link IdGenerator}-ът, така че няма дублиране на ID-та –
 * каквото и място да създава граматика, ID-то идва само от едно място.</p>
 */
public class GrammarRepository {

    private final Map<Integer, Grammar> grammars = new LinkedHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    /**
     * @return генераторът на ID-та (използва се от parser-а и operations service-а)
     */
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     * Добавя нова граматика. Ако ID вече съществува – грешка.
     *
     * @param grammar обектът граматика
     */
    public void add(Grammar grammar) {
        if (grammars.containsKey(grammar.getId())) {
            throw new IllegalStateException("Граматика с ID " + grammar.getId() + " вече съществува.");
        }
        grammars.put(grammar.getId(), grammar);
    }

    /**
     * Връща граматика по ID или хвърля {@link GrammarNotFoundException}.
     *
     * @param id ID на граматика
     * @return обектът граматика
     */
    public Grammar getOrThrow(int id) {
        Grammar g = grammars.get(id);
        if (g == null) {
            throw new GrammarNotFoundException(id);
        }
        return g;
    }

    /**
     * @return имутабилен изглед към всички граматики, в реда на добавяне
     */
    public List<Grammar> all() {
        return Collections.unmodifiableList(new ArrayList<>(grammars.values()));
    }

    /**
     * Изтрива всички граматики (използва се от <code>close</code>).
     */
    public void clear() {
        grammars.clear();
    }

    /**
     * @return броят на заредените граматики
     */
    public int size() {
        return grammars.size();
    }

    /**
     * @return true ако няма заредени граматики
     */
    public boolean isEmpty() {
        return grammars.isEmpty();
    }
}
