package bg.tu_varna.sit.f24621674.util;


/**
 * Прост генератор на уникални цели ID-та за граматики.
 *
 * <p>Използва <code>long</code>-базиран брояч, но връща <code>int</code> – за
 * един сесиен живот на приложението това е повече от достатъчно.
 * Няма нужда от thread-safety, защото цялата работа е в един thread (CLI).</p>
 */
public final class IdGenerator {

    private int next;

    /**
     * Създава нов генератор, който стартира от 1.
     */
    public IdGenerator() {
        this.next = 1;
    }

    /**
     * Връща следващото уникално ID и увеличава брояча.
     *
     * @return ново уникално ID
     */
    public int nextId() {
        return next++;
    }
}
