import java.util.HashMap;
import java.util.Map;

public class CoinReceiver {
    private int maxCapacity;
    private Map<Integer, Integer> insertedCoins;

    public CoinReceiver(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.insertedCoins = new HashMap<>();
        clear();
    }

    public boolean insertCoin(int denomination) {
        if (getTotalCount() >= maxCapacity) {
            return false;
        }
        insertedCoins.put(denomination, insertedCoins.getOrDefault(denomination, 0) + 1);
        return true;
    }

    public int getTotalCount() {
        return insertedCoins.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getSum() {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : insertedCoins.entrySet()) {
            sum += entry.getKey() * entry.getValue();
        }
        return sum;
    }

    public Map<Integer, Integer> getCoins() {
        return new HashMap<>(insertedCoins);
    }

    public void clear() {
        insertedCoins.put(10, 0);
        insertedCoins.put(5, 0);
        insertedCoins.put(2, 0);
        insertedCoins.put(1, 0);
    }
}
