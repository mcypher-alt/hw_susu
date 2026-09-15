import java.util.ArrayList;
import java.util.Map;

public class VendingMachine {
    private ArrayList<ProductSlot> slots;
    private CashBox cashBox;
    private CoinReceiver receiver;

    public VendingMachine(int receiverCapacity) {
        slots = new ArrayList<>();
        slots.add(new ProductSlot(1, "Вода", 75, 2));
        slots.add(new ProductSlot(2, "Сок", 60, 4));
        slots.add(new ProductSlot(3, "Шоколад", 120, 8));
        slots.add(new ProductSlot(4, "Вода", 75, 16));

        cashBox = new CashBox(10, 10, 10, 10);
        receiver = new CoinReceiver(receiverCapacity);
    }

    public void showProducts() {
        System.out.println("№\t| Название товара\t| Цена, руб.\t| Количество");
        for (ProductSlot slot : slots) {
            System.out.println(slot.toString());
        }
    }

    public void insertCoin(int denomination) {
        if (receiver.insertCoin(denomination)) {
            System.out.println("Монета принята.");
        } else {
            System.out.println("Ошибка: приемное отделение переполнено (макс. " + receiver.getTotalCount() + " монет). Монета отклонена.");
        }
    }

    public void buyProduct(int slotId) {
        ProductSlot targetSlot = null;
        for (ProductSlot s : slots) {
            if (s.getId() == slotId) {
                targetSlot = s;
                break;
            }
        }

        if (targetSlot == null) {
            System.out.println("Отказ: ячейка с таким номером не существует.");
            return;
        }
        if (targetSlot.getQuantity() == 0) {
            System.out.println("Отказ: товар закончился.");
            return;
        }
        if (receiver.getSum() < targetSlot.getPrice()) {
            System.out.println("Отказ: недостаточно средств.");
            return;
        }

        int changeAmount = receiver.getSum() - targetSlot.getPrice();
        Map<Integer, Integer> change = cashBox.calculateChange(changeAmount, receiver.getCoins());

        if (change == null) {
            System.out.println("Отказ: невозможно выдать сдачу. Состояние сохранено.");
            return;
        }

        targetSlot.reduceQuantity();
        cashBox.addCoins(receiver.getCoins());
        cashBox.removeCoins(change);
        receiver.clear();

        System.out.println("Успешная покупка! Выдан товар: " + targetSlot.getName() + " (Ячейка " + targetSlot.getId() + ")");
        if (changeAmount > 0) {
            System.out.println("Сумма сдачи: " + changeAmount + " руб. Состав: " + formatCoins(change));
        } else {
            System.out.println("Сдача не требуется.");
        }
    }

    public void returnCoins() {
        if (receiver.getSum() == 0) {
            System.out.println("Отказ: нет внесенных монет для возврата.");
            return;
        }
        System.out.println("Возвращены монеты: " + formatCoins(receiver.getCoins()));
        receiver.clear();
    }

    public void showCashBoxState() {
        System.out.println(cashBox.toString());
    }

    public int getInsertedSum() {
        return receiver.getSum();
    }

    private String formatCoins(Map<Integer, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (int d : new int[]{10, 5, 2, 1}) {
            int count = map.getOrDefault(d, 0);
            if (count > 0) {
                sb.append(d).append("р-").append(count).append("шт ");
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return "Торговый автомат (Ячеек: " + slots.size() + ", Внесено: " + receiver.getSum() + " руб.)";
    }
}
