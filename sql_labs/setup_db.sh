#!/bin/bash
set -e

echo "=== 1. Создание пользователя, базы и выдача прав ==="
sudo -u postgres psql << 'SQL_INIT'
-- Удаляем базу и юзера, если остались от прошлых попыток (для чистого запуска)
DROP DATABASE IF EXISTS supplies_db;
DROP USER IF EXISTS supply_admin;

-- Создание пользователя с паролем
CREATE USER supply_admin WITH PASSWORD 'supply_pass_123';

-- Создание рабочей базы данных с владельцем supply_admin
CREATE DATABASE supplies_db OWNER supply_admin;

-- Выдача всех привилегий
GRANT ALL PRIVILEGES ON DATABASE supplies_db TO supply_admin;
SQL_INIT

echo "=== 2. Создание таблиц, ограничений и комментариев ==="
sudo -u postgres psql -d supplies_db << 'SQL_SCHEMA'
-- Настройка прав на схему public для владельца
GRANT ALL ON SCHEMA public TO supply_admin;

-- Таблица 1: Поставщики (suppliers)
CREATE TABLE suppliers (
    supplier_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL DEFAULT 'неизвестен',
    rating INT NOT NULL,

    -- Ограничения целостности
    CONSTRAINT uq_supplier_name_city UNIQUE (name, city),
    CONSTRAINT chk_supplier_rating CHECK (rating BETWEEN 1 AND 10)
);

COMMENT ON TABLE suppliers IS 'Справочник поставщиков материалов и оборудования';
COMMENT ON COLUMN suppliers.supplier_id IS 'Первичный ключ: уникальный идентификатор поставщика';
COMMENT ON COLUMN suppliers.name IS 'Наименование организации поставщика (NOT NULL)';
COMMENT ON COLUMN suppliers.city IS 'Город базирования поставщика (NOT NULL)';
COMMENT ON COLUMN suppliers.address IS 'Адрес поставщика (NOT NULL, по умолчанию: неизвестен)';
COMMENT ON COLUMN suppliers.rating IS 'Рейтинг поставщика от 1 до 10 (NOT NULL)';


-- Таблица 2: Детали (parts)
CREATE TABLE parts (
    part_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    weight NUMERIC(10, 3) NOT NULL,
    color VARCHAR(30) NOT NULL,

    -- Ограничения целостности
    CONSTRAINT chk_part_price CHECK (price > 0),
    CONSTRAINT chk_part_weight CHECK (weight > 0),
    CONSTRAINT chk_part_color CHECK (color IN (
        'белый', 'черный', 'красный', 'синий', 
        'серый', 'зеленый', 'желтый', 'оранжевый'
    ))
);

COMMENT ON TABLE parts IS 'Номенклатурный справочник деталей';
COMMENT ON COLUMN parts.part_id IS 'Первичный ключ: уникальный идентификатор детали';
COMMENT ON COLUMN parts.name IS 'Наименование детали (NOT NULL)';
COMMENT ON COLUMN parts.price IS 'Цена детали (NOT NULL, строго > 0)';
COMMENT ON COLUMN parts.weight IS 'Вес детали (NOT NULL, строго > 0)';
COMMENT ON COLUMN parts.color IS 'Цвет детали из строго фиксированного списка';


-- Таблица 3: Проекты (projects)
CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    budget NUMERIC(15, 2) NOT NULL,

    -- Ограничения целостности
    CONSTRAINT chk_project_budget CHECK (budget > 0)
);

COMMENT ON TABLE projects IS 'Реестр строительно-монтажных проектов';
COMMENT ON COLUMN projects.project_id IS 'Первичный ключ: уникальный идентификатор проекта';
COMMENT ON COLUMN projects.name IS 'Название строительного объекта/проекта (NOT NULL)';
COMMENT ON COLUMN projects.city IS 'Город проекта (NOT NULL)';
COMMENT ON COLUMN projects.address IS 'Адрес стройплощадки (NOT NULL)';
COMMENT ON COLUMN projects.budget IS 'Бюджет проекта в рублях (NOT NULL, строго > 0)';


-- Таблица 4: Поставки (shipments)
CREATE TABLE shipments (
    shipment_id SERIAL PRIMARY KEY,
    supplier_id INT NOT NULL,
    part_id INT NOT NULL,
    project_id INT NOT NULL,
    quantity INT NOT NULL,
    shipment_date DATE NOT NULL DEFAULT CURRENT_DATE,

    -- Ограничение количества
    CONSTRAINT chk_shipment_quantity CHECK (quantity > 0),

    -- Внешние ключи с каскадным удалением и обновлением
    CONSTRAINT fk_shipment_supplier FOREIGN KEY (supplier_id) 
        REFERENCES suppliers(supplier_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,

    CONSTRAINT fk_shipment_part FOREIGN KEY (part_id) 
        REFERENCES parts(part_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,

    CONSTRAINT fk_shipment_project FOREIGN KEY (project_id) 
        REFERENCES projects(project_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

COMMENT ON TABLE shipments IS 'Журнал поставок деталей под проекты';
COMMENT ON COLUMN shipments.shipment_id IS 'Первичный ключ записи о поставке';
COMMENT ON COLUMN shipments.supplier_id IS 'Внешний ключ: ссылка на поставщика';
COMMENT ON COLUMN shipments.part_id IS 'Внешний ключ: ссылка на деталь';
COMMENT ON COLUMN shipments.project_id IS 'Внешний ключ: ссылка на проект';
COMMENT ON COLUMN shipments.quantity IS 'Количество единиц деталей (NOT NULL, строго > 0)';
COMMENT ON COLUMN shipments.shipment_date IS 'Дата поставки';

-- Назначение владельца таблиц
ALTER TABLE suppliers OWNER TO supply_admin;
ALTER TABLE parts OWNER TO supply_admin;
ALTER TABLE projects OWNER TO supply_admin;
ALTER TABLE shipments OWNER TO supply_admin;
SQL_SCHEMA

echo "=== Готово! Проверяем созданные таблицы ==="
sudo -u postgres psql -d supplies_db -c "\dt"
