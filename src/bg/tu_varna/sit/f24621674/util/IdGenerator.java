package bg.tu_varna.sit.f24621674.util;

/**
 * Генератор на уникални ID-та за граматики
 * Стартира от 1 и увеличава броячa с всяко извикване
 */
public final class IdGenerator {

    private int next;

    /**
     * Създава нов генератор започващ от 1
     */
    public IdGenerator() {
        this.next = 1;
    }

    /**
     * Връща следващото уникално ID и увеличава брояча
     * @return ново уникално ID
     */
    public int nextId() {
        return next++;
    }
}