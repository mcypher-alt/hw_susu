import java.util.HashMap;
import java.util.Map;

public class CashBox {
    private Map<Integer, Integer> coins;

    public CashBox(int c10, int c5, int c2, int c1) {
        coins = new HashMap<>();
        coins.put(10, c10);
        coins.put(5, c5);
        coins.put(2, c2);
        coins.put(1, c1);
    }

    public void addCoins(Map<Integer, Integer> newCoins) {
        for (Map.Entry<Integer, Integer> entry : newCoins.entrySet()) {
            coins.put(entry.getKey(), coins.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
    }

    public void removeCoins(Map<Integer, Integer> toRemove) {
        for (Map.Entry<Integer, Integer> entry : toRemove.entrySet()) {
            coins.put(entry.getKey(), coins.get(entry.getKey()) - entry.getValue());
        }
    }

    public Map<Integer, Integer> calculateChange(int changeNeeded, Map<Integer, Integer> insertedCoins) {
        Map<Integer, Integer> tempPool = new HashMap<>(coins);
        for (Map.Entry<Integer, Integer> entry : insertedCoins.entrySet()) {
            tempPool.put(entry.getKey(), tempPool.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }

        Map<Integer, Integer> changeOut = new HashMap<>();
        int[] denominations = {10, 5, 2, 1};
        int remaining = changeNeeded;

        for (int d : denominations) {
            int available = tempPool.getOrDefault(d, 0);
            int needed = remaining / d;
            int toTake = Math.min(available, needed);

            if (toTake > 0) {
                changeOut.put(d, toTake);
                remaining -= d * toTake;
            }
        }

        if (remaining > 0) {
            return null;
        }
        return changeOut;
    }

    @Override
    public String toString() {
        return String.format("Касса: 10р - %d шт, 5р - %d шт, 2р - %d шт, 1р - %d шт.", 
                coins.get(10), coins.get(5), coins.get(2), coins.get(1));
    }
}
