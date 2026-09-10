#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

// Структура 1: данные датчиков (будет размещена в куче по умолчанию)
typedef struct {
    int id;
    double sensorValue;
    char tag[32];
} SensorData;

// Структура 2: системный лог (будет размещена в дополнительной приватной куче)
typedef struct {
    int eventId;
    DWORD timestamp;
    char description[64];
} LogRecord;

int main(void) {
    // Установка кодовой страницы UTF-8 для корректного отображения кириллицы в консоли Windows
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);

    printf("==============================================================\n");
    printf("  Лабораторная работа: Управление памятью и кучами в WinAPI  \n");
    printf("==============================================================\n\n");

    // 1. Запрос объемов структур данных у пользователя
    int countSensors = 0;
    int countLogs = 0;

    printf("Введите количество элементов для структуры SensorData (куча по умолчанию): ");
    if (scanf("%d", &countSensors) != 1 || countSensors <= 0) {
        fprintf(stderr, "Ошибка: некорректное число элементов.\n");
        return 1;
    }

    printf("Введите количество элементов для структуры LogRecord (дополнительная куча): ");
    if (scanf("%d", &countLogs) != 1 || countLogs <= 0) {
        fprintf(stderr, "Ошибка: некорректное число элементов.\n");
        return 1;
    }

    printf("\n--- 1. Получение и создание дескрипторов куч ---\n");

    // Получаем дескриптор кучи по умолчанию текущего процесса
    HANDLE hDefaultHeap = GetProcessHeap();
    if (hDefaultHeap == NULL) {
        fprintf(stderr, "Ошибка получения кучи по умолчанию. Код ошибки: %lu\n", GetLastError());
        return 1;
    }
    printf("[+] Дескриптор кучи по умолчанию (Default Heap Handle): 0x%p\n", (void*)hDefaultHeap);

    // Создаем дополнительную (частную) динамически расширяемую кучу
    // Флаги: 0 (сериализация доступа включена)
    // dwInitialSize: 0 (1 страница по умолчанию)
    // dwMaximumSize: 0 (куча может расти до пределов виртуальной памяти)
    HANDLE hPrivateHeap = HeapCreate(0, 0, 0);
    if (hPrivateHeap == NULL) {
        fprintf(stderr, "Ошибка создания дополнительной кучи. Код ошибки: %lu\n", GetLastError());
        return 1;
    }
    printf("[+] Дескриптор дополнительной кучи (Private Heap Handle): 0x%p\n", (void*)hPrivateHeap);

    printf("\n--- 2. Динамическое выделение памяти под структуры ---\n");

    // Расчет требуемых размеров памяти в байтах
    SIZE_T sizeSensors = (SIZE_T)countSensors * sizeof(SensorData);
    SIZE_T sizeLogs = (SIZE_T)countLogs * sizeof(LogRecord);

    // Выделение памяти в куче по умолчанию с флагом зануления памяти
    SensorData* pSensors = (SensorData*)HeapAlloc(hDefaultHeap, HEAP_ZERO_MEMORY, sizeSensors);
    if (pSensors == NULL) {
        fprintf(stderr, "Ошибка выделения памяти в куче по умолчанию!\n");
        HeapDestroy(hPrivateHeap);
        return 1;
    }
    printf("[+] Выделено %zu байт под SensorData в дефолтной куче по адресу: 0x%p\n", 
           sizeSensors, (void*)pSensors);

    // Выделение памяти в дополнительной куче
    LogRecord* pLogs = (LogRecord*)HeapAlloc(hPrivateHeap, HEAP_ZERO_MEMORY, sizeLogs);
    if (pLogs == NULL) {
        fprintf(stderr, "Ошибка выделения памяти в дополнительной куче!\n");
        HeapFree(hDefaultHeap, 0, pSensors);
        HeapDestroy(hPrivateHeap);
        return 1;
    }
    printf("[+] Выделено %zu байт под LogRecord в приватной куче по адресу:   0x%p\n", 
           sizeLogs, (void*)pLogs);

    printf("\n--- 3. Заполнение структур данными ---\n");

    // Заполнение структуры 1 (SensorData)
    for (int i = 0; i < countSensors; i++) {
        pSensors[i].id = i + 1;
        pSensors[i].sensorValue = 20.0 + (i * 1.5);
        snprintf(pSensors[i].tag, sizeof(pSensors[i].tag), "Sensor_Zone_%d", i + 1);
    }

    // Заполнение структуры 2 (LogRecord)
    DWORD currentTicks = GetTickCount();
    for (int i = 0; i < countLogs; i++) {
        pLogs[i].eventId = 1000 + i;
        pLogs[i].timestamp = currentTicks + (DWORD)(i * 250);
        snprintf(pLogs[i].description, sizeof(pLogs[i].description), "System Event #%d triggered", i + 1);
    }

    // Демонстрация вывода первых элементов
    printf("\nСодержимое первых элементов SensorData (Куча по умолчанию):\n");
    int printLimitSensors = (countSensors > 3) ? 3 : countSensors;
    for (int i = 0; i < printLimitSensors; i++) {
        printf("  [%d] ID: %d | Tag: %-15s | Value: %.2f | Адрес: 0x%p\n",
               i, pSensors[i].id, pSensors[i].tag, pSensors[i].sensorValue, (void*)&pSensors[i]);
    }

    printf("\nСодержимое первых элементов LogRecord (Дополнительная куча):\n");
    int printLimitLogs = (countLogs > 3) ? 3 : countLogs;
    for (int i = 0; i < printLimitLogs; i++) {
        printf("  [%d] EventID: %d | Time: %lu ms | Desc: %-25s | Адрес: 0x%p\n",
               i, pLogs[i].eventId, pLogs[i].timestamp, pLogs[i].description, (void*)&pLogs[i]);
    }

    printf("\n--- 4. Освобождение выделенных ресурсов ---\n");

    // 1. Освобождение блока памяти в дефолтной куче
    if (HeapFree(hDefaultHeap, 0, pSensors)) {
        printf("[OK] Память pSensors в куче по умолчанию успешно освобождена.\n");
        pSensors = NULL;
    } else {
        fprintf(stderr, "Ошибка освобождения памяти pSensors. Код: %lu\n", GetLastError());
    }

    // 2. Освобождение блока памяти в приватной куче
    if (HeapFree(hPrivateHeap, 0, pLogs)) {
        printf("[OK] Память pLogs в дополнительной куче успешно освобождена.\n");
        pLogs = NULL;
    } else {
        fprintf(stderr, "Ошибка освобождения памяти pLogs. Код: %lu\n", GetLastError());
    }

    // 3. Уничтожение дополнительной кучи целиком
    if (HeapDestroy(hPrivateHeap)) {
        printf("[OK] Дополнительная куча успешно уничтожена (HeapDestroy).\n");
        hPrivateHeap = NULL;
    } else {
        fprintf(stderr, "Ошибка уничтожения дополнительной кучи. Код: %lu\n", GetLastError());
    }

    printf("\nРабота программы успешно завершена. Все ресурсы освобождены.\n");
    return 0;
}