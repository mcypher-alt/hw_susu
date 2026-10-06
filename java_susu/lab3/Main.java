import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Route route = new Route(100.0);
    private static ArrayList<Vehicle> fleet = new ArrayList<>();

    public static void main(String[] args) {
        // Начальная конфигурация
        route.addGasStation(20.0);
        route.addGasStation(50.0);
        route.addGasStation(80.0);

        fleet.add(new BaggageBus(1, 0.0, 120.0, 60.0, 20.0, route, 40, 0, 0.1, 1.5, 2.0));
        fleet.add(new Truck(2, 20.0, 200.0, 100.0, 25.0, route, 10.0, 4.0, 1.5));

        boolean running = true;
        while (running) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. Выбрать транспорт");
            System.out.println("2. Добавить транспорт");
            System.out.println("3. Маршрут и заправки");
            System.out.println("0. Завершить работу");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": selectVehicleMenu(); break;
                case "2": addVehicleMenu(); break;
                case "3": routeMenu(); break;
                case "0": running = false; break;
                default: System.out.println("Неверная команда.");
            }
        }
    }

    private static void selectVehicleMenu() {
        System.out.println("\n--- Список транспорта ---");
        for (Vehicle v : fleet) {
            System.out.println(v);
        }
        System.out.print("Введите номер транспорта (или 0 для возврата): ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) return;
            Vehicle selected = null;
            for (Vehicle v : fleet) {
                if (v.getId() == id) {
                    selected = v;
                    break;
                }
            }
            if (selected == null) {
                System.out.println("Транспорт с таким номером не найден.");
                return;
            }
            vehicleActionsMenu(selected);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
        }
    }

    private static void vehicleActionsMenu(Vehicle v) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.printf("\nВыбран транспорт №%d (%s)\n", v.getId(), v.getClass().getSimpleName());
            System.out.println("1. Показать состояние");
            System.out.println("2. Поехать");
            System.out.println("3. Заправить");

            if (v instanceof BaggageBus) {
                System.out.println("4. Посадить пассажиров с багажом");
                System.out.println("5. Высадить пассажиров (FIFO)");
            } else if (v instanceof Bus) {
                System.out.println("4. Посадить пассажиров");
                System.out.println("5. Высадить пассажиров");
            } else if (v instanceof Truck) {
                System.out.println("4. Загрузить груз");
                System.out.println("5. Разгрузить груз");
            }
            System.out.println("0. Вернуться в главное меню");
            System.out.print("Выберите команду: ");

            String cmd = scanner.nextLine();
            switch (cmd) {
                case "1":
                    System.out.println(v);
                    break;
                case "2":
                    handleMove(v);
                    break;
                case "3":
                    handleRefuel(v);
                    break;
                case "4":
                    handleLoad(v);
                    break;
                case "5":
                    handleUnload(v);
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Неверный пункт.");
            }
        }
    }

    private static void handleMove(Vehicle v) {
        System.out.print("Введите координату назначения (0 - " + route.getLength() + "): ");
        try {
            double target = Double.parseDouble(scanner.nextLine());
            if (target < 0 || target > route.getLength()) {
                System.out.println("Координата выходит за пределы маршрута.");
                return;
            }

            double dist = Math.abs(target - v.getPosition());
            double neededFuel = (dist * v.getFuelConsumption()) / 100.0;

            if (v.getFuel() < neededFuel) {
                double maxDist = v.getMaxDistance();
                System.out.printf("Водитель: «Похоже, топлива на всю поездку не хватит.\nЯ смогу проехать только %.1f км из %.1f км.\nЛучше сначала заехать на заправку».\n",
                        maxDist, dist);
                System.out.println("1. Все равно поехать\n2. Отменить поездку");
                System.out.print("Выбор: ");
                String sub = scanner.nextLine();
                if ("1".equals(sub)) {
                    v.move(target, true);
                    System.out.println("Поездка завершена (бак пуст): " + v);
                } else {
                    System.out.println("Поездка отменена.");
                }
            } else {
                v.move(target, false);
                System.out.println("Успешно прибыли: " + v);
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат числа.");
        }
    }

    private static void handleRefuel(Vehicle v) {
        if (!route.hasGasStation(v.getPosition())) {
            System.out.println("Ошибка: на текущей координате (" + v.getPosition() + " км) нет заправки.");
            return;
        }
        System.out.print("Сколько литров заправить: ");
        try {
            double lit = Double.parseDouble(scanner.nextLine());
            if (v.refuel(lit)) {
                System.out.println("Заправка выполнена: " + v);
            } else {
                System.out.println("Не удалось заправить (проверьте объем).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат.");
        }
    }

    private static void handleLoad(Vehicle v) {
        if (v instanceof BaggageBus) {
            BaggageBus bb = (BaggageBus) v;
            try {
                System.out.print("Количество пассажиров: ");
                int p = Integer.parseInt(scanner.nextLine());
                System.out.print("Багаж каждого (в тоннах, напр. 0.02): ");
                double w = Double.parseDouble(scanner.nextLine());
                int boarded = bb.boardWithBaggage(p, w);
                System.out.printf("Посажено %d пассажиров с багажом. Текущее состояние: %s\n", boarded, bb);
            } catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        } else if (v instanceof Bus) {
            Bus b = (Bus) v;
            try {
                System.out.print("Количество пассажиров: ");
                int p = Integer.parseInt(scanner.nextLine());
                int boarded = b.boardPassengers(p);
                System.out.printf("Посажено %d пассажиров.\n", boarded);
            } catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        } else if (v instanceof Truck) {
            Truck t = (Truck) v;
            try {
                System.out.print("Масса груза (т): ");
                double m = Double.parseDouble(scanner.nextLine());
                double loaded = t.loadCargo(m);
                System.out.printf("Загружено %.2f т груза.\n", loaded);
            } catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        }
    }

    private static void handleUnload(Vehicle v) {
        if (v instanceof BaggageBus) {
            BaggageBus bb = (BaggageBus) v;
            try {
                System.out.print("Сколько пассажиров высадить: ");
                int p = Integer.parseInt(scanner.nextLine());
                int unboarded = bb.unboardFIFO(p);
                System.out.printf("Высажено %d пассажиров с их багажом. Состояние: %s\n", unboarded, bb);
            } catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        } else if (v instanceof Bus) {
            Bus b = (Bus) v;
            try {
                System.out.print("Сколько пассажиров высадить: ");
                int p = Integer.parseInt(scanner.nextLine());
                int unb = b.unboardPassengers(p);
                System.out.printf("Высажено %d пассажиров.\n", unb);
            } catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        } else if (v instanceof Truck) {
            Truck t = (Truck) v;
            try {
                System.out.print("Сколько тонн разгрузить: ");
                double m = Double.parseDouble(scanner.nextLine());
                double unl = t.unloadCargo(m);
                System.out.printf("Разгружено %.2f т.\n", unl);
            } catch (Exception e) {
                System.out.println("Ошибка ввода.");
            }
        }
    }

    private static void addVehicleMenu() {
        System.out.println("\n--- Добавление транспорта ---");
        System.out.println("1. Автобус");
        System.out.println("2. Грузовик");
        System.out.println("3. Междугородний автобус с багажом (Вар 6)");
        System.out.println("0. Назад");
        System.out.print("Выбор: ");
        String t = scanner.nextLine();
        if ("0".equals(t)) return;

        try {
            System.out.print("Уникальный номер (ID): ");
            int id = Integer.parseInt(scanner.nextLine());
            for (Vehicle v : fleet) {
                if (v.getId() == id) {
                    System.out.println("Ошибка: транспорт с таким ID уже существует.");
                    return;
                }
            }
            System.out.print("Начальная позиция (0-" + route.getLength() + "): ");
            double pos = Double.parseDouble(scanner.nextLine());
            System.out.print("Вместимость бака (л): ");
            double tank = Double.parseDouble(scanner.nextLine());
            System.out.print("Текущее топливо (л): ");
            double fuel = Double.parseDouble(scanner.nextLine());
            System.out.print("Базовый расход (л/100км): ");
            double baseCons = Double.parseDouble(scanner.nextLine());

            if ("1".equals(t)) {
                System.out.print("Макс. пассажиров: ");
                int maxP = Integer.parseInt(scanner.nextLine());
                System.out.print("Текущие пассажиры: ");
                int curP = Integer.parseInt(scanner.nextLine());
                System.out.print("Надбавка за пассажира (л/100км): ");
                double fee = Double.parseDouble(scanner.nextLine());
                fleet.add(new Bus(id, pos, tank, fuel, baseCons, route, maxP, curP, fee));
                System.out.println("Автобус добавлен.");
            } else if ("2".equals(t)) {
                System.out.print("Грузоподъемность (т): ");
                double maxC = Double.parseDouble(scanner.nextLine());
                System.out.print("Текущий груз (т): ");
                double curC = Double.parseDouble(scanner.nextLine());
                System.out.print("Надбавка за тонну (л/100км): ");
                double fee = Double.parseDouble(scanner.nextLine());
                fleet.add(new Truck(id, pos, tank, fuel, baseCons, route, maxC, curC, fee));
                System.out.println("Грузовик добавлен.");
            } else if ("3".equals(t)) {
                System.out.print("Макс. пассажиров: ");
                int maxP = Integer.parseInt(scanner.nextLine());
                System.out.print("Надбавка за пассажира (л/100км): ");
                double pFee = Double.parseDouble(scanner.nextLine());
                System.out.print("Вместимость багажного отсека (т): ");
                double maxB = Double.parseDouble(scanner.nextLine());
                System.out.print("Надбавка за тонну багажа (л/100км): ");
                double bFee = Double.parseDouble(scanner.nextLine());
                fleet.add(new BaggageBus(id, pos, tank, fuel, baseCons, route, maxP, 0, pFee, maxB, bFee));
                System.out.println("Междугородний автобус добавлен.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при вводе параметров.");
        }
    }

    private static void routeMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Маршрут и заправки ---");
            System.out.println("1. Показать маршрут и заправки");
            System.out.println("2. Изменить длину маршрута");
            System.out.println("3. Добавить заправку");
            System.out.println("4. Удалить заправку");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            String cmd = scanner.nextLine();
            switch (cmd) {
                case "1":
                    System.out.println(route);
                    break;
                case "2":
                    System.out.print("Новая длина: ");
                    try {
                        double l = Double.parseDouble(scanner.nextLine());
                        if (route.setLength(l, fleet)) {
                            System.out.println("Длина изменена. Транспорт и заправки актуализированы.");
                        } else {
                            System.out.println("Ошибка изменения длины.");
                        }
                    } catch (Exception e) {
                        System.out.println("Неверный формат.");
                    }
                    break;
                case "3":
                    System.out.print("Координата новой АЗС: ");
                    try {
                        double p = Double.parseDouble(scanner.nextLine());
                        if (route.addGasStation(p)) System.out.println("АЗС добавлена.");
                        else System.out.println("Ошибка добавления АЗС.");
                    } catch (Exception e) {
                        System.out.println("Неверный формат.");
                    }
                    break;
                case "4":
                    System.out.print("Координата удаляемой АЗС: ");
                    try {
                        double p = Double.parseDouble(scanner.nextLine());
                        if (route.removeGasStation(p)) System.out.println("АЗС удалена.");
                        else System.out.println("АЗС не найдена.");
                    } catch (Exception e) {
                        System.out.println("Неверный формат.");
                    }
                    break;
                case "0":
                    inMenu = false;
                    break;
            }
        }
    }
}
