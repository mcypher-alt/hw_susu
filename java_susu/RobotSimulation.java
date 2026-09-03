import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RobotSimulation {

    // Класс для хранения информации о роботе[cite: 1]
    static class Robot {
        String name;
        int x, y;
        char dir;

        // Поля для хранения предполагаемых действий на текущем такте[cite: 1]
        int intendedX, intendedY;
        char intendedDir;

        public Robot(String name, int x, int y, char dir) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        @Override
        public String toString() {
            return name + " " + x + " " + y + " " + dir;
        }
    }

    public static void main(String[] args) {
        // Исходная карта (без роботов)[cite: 1]
        String[] map = {
            "#########",
            "#...#...#",
            "#...#...#",
            "#..>>>..#",
            "#...#...#",
            "#..<<<..#",
            "#...#...#",
            "#...#...#",
            "#########"
        };

        // Начальное состояние роботов[cite: 1]
        String[] robotData = {
            "A 1 1 S",
            "B 1 7 N",
            "C 7 1 S",
            "D 7 7 N"
        };

        // Команды для перевода в конечное состояние (Вариант 7)[cite: 1]
        String[] commands = {
            "A:F B:F C:F D:F", // Такт 1
            "A:F B:F C:F D:F", // Такт 2
            "A:L B:F C:F D:L", // Такт 3
            "A:F B:F C:F D:F", // Такт 4
            "A:F B:R C:R D:F", // Такт 5
            "A:F B:F C:F D:F", // Такт 6
            "A:F B:F C:F D:F", // Такт 7
            "A:F B:F C:F D:F", // Такт 8
            "A:F B:F C:F D:R", // Такт 9
            "A:R B:F C:F D:F", // Такт 10
            "A:F B:L C:L D:F", // Такт 11
            "A:F B:F C:F D:F", // Такт 12
            "A:F"              // Такт 13 (остальные стоят)
        };

        ArrayList<Robot> robots = new ArrayList<>();
        for (String data : robotData) {
            String[] parts = data.split(" ");
            robots.add(new Robot(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3].charAt(0)));
        }

        System.out.println("--- НАЧАЛЬНОЕ СОСТОЯНИЕ ---");
        printState(map, robots);

        // Обработка каждого такта
        for (int t = 0; t < commands.length; t++) {
            System.out.println("\n--- Такт " + (t + 1) + " ---");
            System.out.println("Команды: " + commands[t]);
            processTick(commands[t], map, robots);
            printState(map, robots);
        }
    }

    private static void processTick(String commandStr, String[] map, ArrayList<Robot> robots) {
        Map<String, Character> cmds = new HashMap<>();
        for (String part : commandStr.split(" ")) {
            if (part.contains(":")) {
                String[] rc = part.split(":");
                cmds.put(rc[0], rc[1].charAt(0));
            }
        }

        // Шаг 1: Определение предполагаемых действий[cite: 1]
        for (Robot r : robots) {
            char cmd = cmds.getOrDefault(r.name, 'S'); // По умолчанию робот стоит
            r.intendedDir = r.dir;
            r.intendedX = r.x;
            r.intendedY = r.y;

            if (cmd == 'L') {
                r.intendedDir = turnLeft(r.dir);
            } else if (cmd == 'R') {
                r.intendedDir = turnRight(r.dir);
            } else if (cmd == 'F') {
                int nx = r.x;
                int ny = r.y;
                if (r.dir == 'N') ny--;
                else if (r.dir == 'E') nx++;
                else if (r.dir == 'S') ny++;
                else if (r.dir == 'W') nx--;

                // Проверка выхода за пределы, стен и односторонних клеток[cite: 1]
                if (isValidMove(nx, ny, r.dir, map)) {
                    r.intendedX = nx;
                    r.intendedY = ny;
                }
            }
        }

        // Шаг 2: Разрешение конфликтов при перемещении[cite: 1]
        boolean changed;
        do {
            changed = false;
            
            // Правило 3: Если несколько роботов пытаются занять одну клетку - отмена[cite: 1]
            Map<String, Integer> targetCounts = new HashMap<>();
            for (Robot r : robots) {
                String target = r.intendedX + "," + r.intendedY;
                targetCounts.put(target, targetCounts.getOrDefault(target, 0) + 1);
            }
            
            for (Robot r : robots) {
                if (r.intendedX != r.x || r.intendedY != r.y) {
                    if (targetCounts.get(r.intendedX + "," + r.intendedY) > 1) {
                        r.intendedX = r.x;
                        r.intendedY = r.y;
                        changed = true;
                    }
                }
            }

            // Правило 6: Запрет обмена позициями[cite: 1]
            for (int i = 0; i < robots.size(); i++) {
                for (int j = i + 1; j < robots.size(); j++) {
                    Robot r1 = robots.get(i);
                    Robot r2 = robots.get(j);
                    if (r1.intendedX == r2.x && r1.intendedY == r2.y && 
                        r2.intendedX == r1.x && r2.intendedY == r1.y) {
                        if (r1.intendedX != r1.x || r2.intendedX != r2.x) {
                            r1.intendedX = r1.x; r1.intendedY = r1.y;
                            r2.intendedX = r2.x; r2.intendedY = r2.y;
                            changed = true;
                        }
                    }
                }
            }

            // Правило 4, 5: Робот не может войти в клетку, если другой робот ее не покинул[cite: 1]
            for (Robot r1 : robots) {
                if (r1.intendedX != r1.x || r1.intendedY != r1.y) {
                    for (Robot r2 : robots) {
                        if (r1 != r2 && r1.intendedX == r2.x && r1.intendedY == r2.y) {
                            if (r2.intendedX == r2.x && r2.intendedY == r2.y) {
                                r1.intendedX = r1.x;
                                r1.intendedY = r1.y;
                                changed = true;
                            }
                        }
                    }
                }
            }
        } while (changed);

        // Шаг 3: Применение подтвержденных действий[cite: 1]
        for (Robot r : robots) {
            r.x = r.intendedX;
            r.y = r.intendedY;
            r.dir = r.intendedDir;
        }
    }

    private static boolean isValidMove(int nx, int ny, char dir, String[] map) {
        if (ny < 0 || ny >= map.length || nx < 0 || nx >= map[ny].length()) return false; // Правило 1[cite: 1]
        char cell = map[ny].charAt(nx);
        if (cell == '#') return false; // Правило 2[cite: 1]
        
        // Вариант 7: Односторонние клетки[cite: 1]
        if (cell == '>') return dir == 'E';
        if (cell == '<') return dir == 'W';
        if (cell == '^') return dir == 'N';
        if (cell == 'v' || cell == 'V') return dir == 'S';
        
        return true;
    }

    private static char turnLeft(char dir) {
        switch (dir) {
            case 'N': return 'W';
            case 'W': return 'S';
            case 'S': return 'E';
            case 'E': return 'N';
            default: return dir;
        }
    }

    private static char turnRight(char dir) {
        switch (dir) {
            case 'N': return 'E';
            case 'E': return 'S';
            case 'S': return 'W';
            case 'W': return 'N';
            default: return dir;
        }
    }

    private static void printState(String[] map, ArrayList<Robot> robots) {
        char[][] grid = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            grid[i] = map[i].toCharArray();
        }

        // Наносим роботов на карту[cite: 1]
        for (Robot r : robots) {
            grid[r.y][r.x] = r.name.charAt(0);
        }

        for (char[] row : grid) {
            System.out.println(new String(row));
        }

        for (Robot r : robots) {
            System.out.println(r);
        }
    }
}