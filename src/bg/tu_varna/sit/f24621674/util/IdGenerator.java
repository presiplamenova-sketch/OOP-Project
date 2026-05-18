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

    /**
     * Синхронизира брояча така че следващото ID да е поне minValue
     */
    public void syncTo(int minValue) {
        if (minValue > next) {
            next = minValue;
        }
    }

    /** Нулира брояча обратно към 1 */
    public void reset() {
        next = 1;
    }


}