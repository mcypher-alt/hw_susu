import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Route route = new Route(100.0);
    private static Dispatcher dispatcher = new Dispatcher();

    public static void main(String[] args) {
        route.addGasStation(20.0);
        route.addGasStation(50.0);
        route.addGasStation(80.0);

        dispatcher.addVehicle(new Bus(1, 0.0, 100.0, 50.0, 15.0, route, 30, 10, 0.1));
        dispatcher.addVehicle(new Truck(2, 20.0, 200.0, 80.0, 25.0, route, 15.0, 5.0, 1.5));
        dispatcher.addVehicle(new Bus(3, 10.0, 100.0, 0.0, 15.0, route, 40, 0, 0.1)); // автобус с пустым баком для теста помощи

        boolean running = true;
        while (running) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. Выбрать транспорт");
            System.out.println("2. Добавить транспорт");
            System.out.println("3. Маршрут и заправки");
            System.out.println("4. Отправить парк в точку");
            System.out.println("5. Показать сводку");
            System.out.println("6. Отправить парк с учетом вместимости точки (Вариант 6)");
            System.out.println("0. Завершить работу");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": selectVehicleMenu(); break;
                case "2": addVehicleMenu(); break;
                case "3": routeMenu(); break;
                case "4":
                    System.out.print("Введите целевую координату маршрута (0 - " + route.getLength() + "): ");
                    try {
                        double target = Double.parseDouble(scanner.nextLine());
                        dispatcher.dispatchFleet(target, scanner);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат числа.");
                    }
                    break;
                case "5": dispatcher.showSummary(); break;
                case "6":
                    System.out.print("Введите целевую координату маршрута (0 - " + route.getLength() + "): ");
                    try {
                        double target = Double.parseDouble(scanner.nextLine());
                        System.out.print("Введите максимальную вместимость точки (количество машин): ");
                        int cap = Integer.parseInt(scanner.nextLine());
                        dispatcher.dispatchFleetWithCapacity(target, cap, scanner);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат числа.");
                    }
                    break;
                case "0":
                    running = false;
                    System.out.println("Работа завершена.");
                    break;
                default:
                    System.out.println("Неизвестная команда.");
            }
        }
        scanner.close();
    }

    private static void selectVehicleMenu() {
        System.out.println("\n--- Список доступного транспорта ---");
        for (Vehicle v : dispatcher.getFleet()) {
            System.out.println(v);
        }
        System.out.print("Введите номер транспорта (или 0 для возврата): ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) return;
            Vehicle selected = dispatcher.findById(id);
            if (selected == null) {
                System.out.println("Транспорт с указанным ID не найден.");
                return;
            }
            vehicleActionsMenu(selected);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод ID.");
        }
    }

    private static void vehicleActionsMenu(Vehicle v) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.printf("\nВыбран транспорт №%d (%s)\n", v.getId(), v.getClass().getSimpleName());
            System.out.println("1. Показать состояние");
            System.out.println("2. Поехать");
            System.out.println("3. Заправить");

            if (v instanceof Bus) {
                System.out.println("4. Посадить пассажиров");
                System.out.println("5. Высадить пассажиров");
                System.out.println("6. Вызвать помощь (если бак пуст)");
            } else if (v instanceof Truck) {
                System.out.println("4. Загрузить груз");
                System.out.println("5. Разгрузить груз");
            }
            System.out.println("0. Вернуться в главное меню");
            System.out.print("Команда: ");

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    System.out.println(v);
                    break;
                case "2":
                    System.out.print("Координата назначения: ");
                    try {
                        double target = Double.parseDouble(scanner.nextLine());
                        double dist = Math.abs(target - v.getPosition());
                        double needed = (dist * v.getFuelConsumption()) / 100.0;
                        if (v.getFuel() < needed) {
                            System.out.printf("Водитель: «Похоже, топлива на всю поездку не хватит.\nЯ смогу проехать только %.1f км из %.1f км».\n",
                                    v.getMaxDistance(), dist);
                            System.out.println("1. Все равно поехать\n2. Отменить поездку");
                            if ("1".equals(scanner.nextLine().trim())) {
                                v.move(target, true);
                            }
                        } else {
                            v.move(target, false);
                        }
                        System.out.println("Текущее состояние: " + v);
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода координаты.");
                    }
                    break;
                case "3":
                    if (!route.hasGasStation(v.getPosition())) {
                        System.out.println("Ошибка: на данной отметке нет заправки.");
                        break;
                    }
                    System.out.print("Количество литров: ");
                    try {
                        double l = Double.parseDouble(scanner.nextLine());
                        v.refuel(l);
                        System.out.println("Состояние: " + v);
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода.");
                    }
                    break;
                case "4":
                    if (v instanceof Bus) {
                        System.out.print("Сколько пассажиров посадить: ");
                        int p = Integer.parseInt(scanner.nextLine());
                        ((Bus) v).boardPassengers(p);
                    } else {
                        System.out.print("Сколько тонн загрузить: ");
                        double m = Double.parseDouble(scanner.nextLine());
                        ((Truck) v).loadCargo(m);
                    }
                    System.out.println("Состояние: " + v);
                    break;
                case "5":
                    if (v instanceof Bus) {
                        System.out.print("Сколько пассажиров высадить: ");
                        int p = Integer.parseInt(scanner.nextLine());
                        ((Bus) v).unboardPassengers(p);
                    } else {
                        System.out.print("Сколько тонн разгрузить: ");
                        double m = Double.parseDouble(scanner.nextLine());
                        ((Truck) v).unloadCargo(m);
                    }
                    System.out.println("Состояние: " + v);
                    break;
                case "6":
                    if (v instanceof Bus) {
                        ((Bus) v).callAssistance();
                    } else {
                        System.out.println("Команда недоступна для грузовика.");
                    }
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Неверная команда.");
            }
        }
    }

    private static void addVehicleMenu() {
        System.out.println("\n--- Добавить транспорт ---");
        System.out.println("1. Автобус\n2. Грузовик\n0. Назад");
        System.out.print("Выбор: ");
        String t = scanner.nextLine().trim();
        if ("0".equals(t)) return;

        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (dispatcher.findById(id) != null) {
                System.out.println("ID уже занят.");
                return;
            }
            System.out.print("Позиция (0 - " + route.getLength() + "): ");
            double pos = Double.parseDouble(scanner.nextLine());
            System.out.print("Емкость бака: ");
            double tank = Double.parseDouble(scanner.nextLine());
            System.out.print("Текущее топливо: ");
            double fuel = Double.parseDouble(scanner.nextLine());
            System.out.print("Базовый расход: ");
            double baseCons = Double.parseDouble(scanner.nextLine());

            if ("1".equals(t)) {
                System.out.print("Макс. пассажиров: ");
                int mp = Integer.parseInt(scanner.nextLine());
                System.out.print("Текущие пассажиры: ");
                int cp = Integer.parseInt(scanner.nextLine());
                System.out.print("Надбавка за пассажира: ");
                double pf = Double.parseDouble(scanner.nextLine());
                dispatcher.addVehicle(new Bus(id, pos, tank, fuel, baseCons, route, mp, cp, pf));
                System.out.println("Автобус добавлен.");
            } else if ("2".equals(t)) {
                System.out.print("Грузоподъемность: ");
                double mc = Double.parseDouble(scanner.nextLine());
                System.out.print("Текущий груз: ");
                double cc = Double.parseDouble(scanner.nextLine());
                System.out.print("Надбавка за тонну: ");
                double cf = Double.parseDouble(scanner.nextLine());
                dispatcher.addVehicle(new Truck(id, pos, tank, fuel, baseCons, route, mc, cc, cf));
                System.out.println("Грузовик добавлен.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода параметров.");
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
            String cmd = scanner.nextLine().trim();

            switch (cmd) {
                case "1":
                    System.out.println(route);
                    break;
                case "2":
                    System.out.print("Новая длина: ");
                    try {
                        double l = Double.parseDouble(scanner.nextLine());
                        if (route.setLength(l, dispatcher.getFleet())) {
                            System.out.println("Длина обновлена.");
                        } else {
                            System.out.println("Недопустимая длина.");
                        }
                    } catch (Exception e) {
                        System.out.println("Ошибка формата.");
                    }
                    break;
                case "3":
                    System.out.print("Координата АЗС: ");
                    try {
                        double p = Double.parseDouble(scanner.nextLine());
                        if (route.addGasStation(p)) System.out.println("АЗС добавлена.");
                        else System.out.println("Ошибка добавления.");
                    } catch (Exception e) {
                        System.out.println("Ошибка формата.");
                    }
                    break;
                case "4":
                    System.out.print("Координата АЗС: ");
                    try {
                        double p = Double.parseDouble(scanner.nextLine());
                        if (route.removeGasStation(p)) System.out.println("АЗС удалена.");
                        else System.out.println("АЗС не найдена.");
                    } catch (Exception e) {
                        System.out.println("Ошибка формата.");
                    }
                    break;
                case "0":
                    inMenu = false;
                    break;
            }
        }
    }
}
