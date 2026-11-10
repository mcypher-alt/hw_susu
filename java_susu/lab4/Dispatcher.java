import java.util.ArrayList;
import java.util.Scanner;

public class Dispatcher {
    private ArrayList<Vehicle> fleet;

    public Dispatcher() {
        this.fleet = new ArrayList<>();
    }

    public void addVehicle(Vehicle v) {
        fleet.add(v);
    }

    public ArrayList<Vehicle> getFleet() {
        return fleet;
    }

    public Vehicle findById(int id) {
        for (Vehicle v : fleet) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    public void showSummary() {
        System.out.println("\n=== СВОДКА ПО ТРАНСПОРТНОМУ ПАРКУ ===");
        int busCount = 0;
        int totalPassengers = 0;
        int truckCount = 0;
        double totalCargo = 0.0;

        for (Vehicle v : fleet) {
            System.out.println(v.toString());
            if (v instanceof Bus) {
                Bus b = (Bus) v;
                busCount++;
                totalPassengers += b.getCurrentPassengers();
            } else if (v instanceof Truck) {
                Truck t = (Truck) v;
                truckCount++;
                totalCargo += t.getCurrentCargo();
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Всего автобусов: %d | Суммарно пассажиров: %d\n", busCount, totalPassengers);
        System.out.printf("Всего грузовиков: %d | Суммарно груза: %.2f т\n", truckCount, totalCargo);
        System.out.println("--------------------------------------------------");
    }

    public void dispatchFleet(double targetPosition, Scanner scanner) {
        executeDispatch(targetPosition, Integer.MAX_VALUE, scanner, false);
    }

    public void dispatchFleetWithCapacity(double targetPosition, int capacity, Scanner scanner) {
        executeDispatch(targetPosition, capacity, scanner, true);
    }

    private void executeDispatch(double targetPosition, int capacityLimit, Scanner scanner, boolean useCapacityLimit) {
        if (fleet.isEmpty()) {
            System.out.println("Парк пуст. Отправка невозможна.");
            return;
        }

        Route route = fleet.get(0).getRoute();
        if (targetPosition < 0 || targetPosition > route.getLength()) {
            System.out.println("Ошибка: целевая координата выходит за границы маршрута. Операция отклонена.");
            return;
        }

        if (useCapacityLimit && capacityLimit < 0) {
            System.out.println("Ошибка: вместимость точки не может быть отрицательной.");
            return;
        }

        int occupiedSlots = 0;
        for (Vehicle v : fleet) {
            if (Math.abs(v.getPosition() - targetPosition) < 0.001) {
                occupiedSlots++;
            }
        }

        System.out.printf("\n--- Запуск рейса в точку %.1f км %s---\n",
                targetPosition, useCapacityLimit ? String.format("(Лимит мест: %d, занято сразу: %d)", capacityLimit, occupiedSlots) : "");

        ArrayList<String> tripReports = new ArrayList<>();
        int successfullyArrivedCount = 0;

        for (Vehicle v : fleet) {
            double startPos = v.getPosition();
            String vehicleType = (v instanceof Bus) ? "Автобус" : "Грузовик";

            if (Math.abs(startPos - targetPosition) < 0.001) {
                tripReports.add(String.format("№%d (%s): Нач: %.1f | Кон: %.1f | Пройдено: 0.0 км | Результат: Уже в точке назначения",
                        v.getId(), vehicleType, startPos, startPos));
                successfullyArrivedCount++;
                continue;
            }

            if (useCapacityLimit && occupiedSlots >= capacityLimit) {
                tripReports.add(String.format("№%d (%s): Нач: %.1f | Кон: %.1f | Пройдено: 0.0 км | Результат: Отклонено (в точке нет свободных мест)",
                        v.getId(), vehicleType, startPos, startPos));
                continue;
            }

            if (v.getFuel() <= 0.001) {
                System.out.printf("Транспорт №%d (%s) не отправлен: пустой бак.\n", v.getId(), vehicleType);
                tripReports.add(String.format("№%d (%s): Нач: %.1f | Кон: %.1f | Пройдено: 0.0 км | Результат: Не отправлен (бак пуст)",
                        v.getId(), vehicleType, startPos, startPos));
                continue;
            }

            double requestedDistance = Math.abs(targetPosition - startPos);
            double consumption = v.getFuelConsumption();
            double neededFuel = (requestedDistance * consumption) / 100.0;

            if (v.getFuel() < neededFuel) {
                double maxDist = v.getMaxDistance();
                System.out.printf("\nВодитель машины №%d: «Похоже, топлива на всю поездку не хватит.\n" +
                                "Я смогу проехать только %.1f км из %.1f км.\n" +
                                "Лучше сначала заехать на заправку».\n",
                        v.getId(), maxDist, requestedDistance);
                System.out.println("1. Все равно поехать\n2. Отменить поездку");
                System.out.print("Выбор: ");
                String choice = scanner.nextLine().trim();

                if ("1".equals(choice)) {
                    v.move(targetPosition, true);
                    double actuallyTraveled = Math.abs(v.getPosition() - startPos);
                    tripReports.add(String.format("№%d (%s): Нач: %.1f | Кон: %.1f | Пройдено: %.1f км | Результат: Остановлен (закончилось топливо)",
                            v.getId(), vehicleType, startPos, v.getPosition(), actuallyTraveled));
                } else {
                    tripReports.add(String.format("№%d (%s): Нач: %.1f | Кон: %.1f | Пройдено: 0.0 км | Результат: Поездка отменена пользователем",
                            v.getId(), vehicleType, startPos, startPos));
                }
            } else {
                v.move(targetPosition, false);
                double actuallyTraveled = Math.abs(v.getPosition() - startPos);
                tripReports.add(String.format("№%d (%s): Нач: %.1f | Кон: %.1f | Пройдено: %.1f км | Результат: Успешно прибыл в точку",
                        v.getId(), vehicleType, startPos, v.getPosition(), actuallyTraveled));
                occupiedSlots++;
                successfullyArrivedCount++;
            }
        }

        System.out.println("\n=== ОТЧЕТ ДИСПЕТЧЕРА О РЕЙСЕ ===");
        for (String rep : tripReports) {
            System.out.println(rep);
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Всего машин, находящихся в точке %.1f км: %d\n", targetPosition, successfullyArrivedCount);
    }
}
