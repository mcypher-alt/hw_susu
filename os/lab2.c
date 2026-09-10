#include <windows.h>
#include <stdio.h>
#include <string.h>

/**
 * Функция логики дочернего процесса.
 * Выполняет задержку в 5 секунд и выводит сообщение "Hello!".
 */
void RunChildProcess(void) {
    // Ожидание длительностью 5000 миллисекунд (5 секунд)
    Sleep(5000);

    // Вывод сообщения в стандартный поток вывода
    printf("Hello!\n");
    fflush(stdout);
}

/**
 * Функция логики родительского процесса.
 * Создает дочерний процесс, ожидает его завершения, выводит "Bye".
 */
int RunParentProcess(const char* exePath) {
    STARTUPINFOA si;
    PROCESS_INFORMATION pi;

    // Инициализация структур нулями
    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory(&pi, sizeof(pi));

    // Формирование командной строки для запуска самого себя с аргументом "child"
    // Пример: "C:\path\to\app.exe" child
    char cmdLine[MAX_PATH * 2];
    snprintf(cmdLine, sizeof(cmdLine), "\"%s\" child", exePath);

    printf("[Parent] Запуск дочернего процесса...\n");

    // 1. Создание нового процесса с помощью CreateProcess
    BOOL success = CreateProcessA(
        NULL,                   // Имя исполняемого модуля (передано через cmdLine)
        cmdLine,                // Командная строка с аргументом child
        NULL,                   // Дескриптор процесса не наследуется
        NULL,                   // Дескриптор потока не наследуется
        FALSE,                  // Запрет наследования дескрипторов
        0,                      // Флаги создания процесса (стандартные)
        NULL,                   // Использовать блок переменных окружения родителя
        NULL,                   // Использовать текущий каталог родителя
        &si,                    // Указатель на структуру STARTUPINFO
        &pi                     // Указатель на структуру PROCESS_INFORMATION
    );

    if (!success) {
        fprintf(stderr, "[Parent ОШИБКА] Не удалось создать процесс. Код ошибки: %lu\n", GetLastError());
        return 1;
    }

    printf("[Parent] Дочерний процесс создан (PID: %lu). Ожидание завершения...\n", pi.dwProcessId);

    // 2. Ожидание завершения дочернего процесса
    WaitForSingleObject(pi.hProcess, INFINITE);

    // 4. Очистка дескрипторов ядра
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

    // 3. Вывод на экран "Bye" после завершения дочернего процесса
    printf("Bye\n");

    return 0;
}

int main(int argc, char* argv[]) {
    // Настройка вывода UTF-8 для корректного отображения кириллицы в консоли
    SetConsoleOutputCP(CP_UTF8);

    // Проверка аргументов: если передан аргумент "child", исполняем ветку дочернего процесса
    if (argc > 1 && strcmp(argv[1], "child") == 0) {
        RunChildProcess();
        return 0;
    }

    // Иначе запускаем сценарий родительского процесса
    return RunParentProcess(argv[0]);
}