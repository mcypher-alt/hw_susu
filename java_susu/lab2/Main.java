import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VendingMachine machine = new VendingMachine(5);

        boolean running = true;
        while (running) {
            System.out.println("\nТекущая внесенная сумма: " + machine.getInsertedSum() + " руб.");
            System.out.println("1. Показать товары");
            System.out.println("2. Внести 1 рубль");
            System.out.println("3. Внести 2 рубля");
            System.out.println("4. Внести 5 рублей");
            System.out.println("5. Внести 10 рублей");
            System.out.println("6. Купить товар");
            System.out.println("7. Вернуть внесенные монеты");
            System.out.println("8. Показать состояние кассы");
            System.out.println("0. Завершить работу");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": 
                    machine.showProducts(); 
                    break;
                case "2": 
                    machine.insertCoin(1); 
                    break;
                case "3": 
                    machine.insertCoin(2); 
                    break;
                case "4": 
                    machine.insertCoin(5); 
                    break;
                case "5": 
                    machine.insertCoin(10); 
                    break;
                case "6":
                    System.out.print("Введите номер ячейки: ");
                    try {
                        int slot = Integer.parseInt(scanner.nextLine());
                        machine.buyProduct(slot);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат номера.");
                    }
                    break;
                case "7": 
                    machine.returnCoins(); 
                    break;
                case "8": 
                    machine.showCashBoxState(); 
                    break;
                case "0":
                    if (machine.getInsertedSum() > 0) {
                        System.out.println("Возврат остатка перед завершением: " + machine.getInsertedSum() + " руб.");
                        machine.returnCoins();
                    }
                    System.out.println("Работа завершена.");
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда.");
            }
        }
        scanner.close();
    }
}
