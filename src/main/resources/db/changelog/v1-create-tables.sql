-- Создание таблицы задач
CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Создание таблицы записей времени
CREATE TABLE IF NOT EXISTS time_records (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_time_range CHECK (end_time > start_time)
    );

-- Создание индексов для производительности
CREATE INDEX IF NOT EXISTS idx_time_records_employee ON time_records(employee_id);
CREATE INDEX IF NOT EXISTS idx_time_records_task ON time_records(task_id);
CREATE INDEX IF NOT EXISTS idx_time_records_period ON time_records(start_time, end_time);

-- Создание индекса для задач по статусу
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);