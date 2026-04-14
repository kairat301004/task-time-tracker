# Task Time Tracker API

## Описание сервиса

Сервис для учета рабочего времени сотрудников над задачами. Позволяет создавать задачи,
отслеживать их статус и фиксировать время, затраченное сотрудниками.

## Реализованный функционал

- Создание задачи
- Получение информации о задаче по ID
- Изменение статуса задачи (NEW -> IN_PROGRESS -> DONE)
- Создание записи о затраченном времени сотрудника на задачу
- Получение информации о затраченном времени сотрудника за период
- JWT аутентификация
- Swagger документация
- Валидация входных данных
- Глобальная обработка ошибок
- Unit тесты (JUnit 5, Mockito)

## Технологии

- Java 17
- Spring Boot 3.2.1
- Spring Security
- MyBatis 3.0.3
- PostgreSQL 15
- Liquibase
- JWT
- Lombok
- MapStruct
- Maven
- JUnit 5, Mockito

## Требования для запуска

- Docker и Docker Compose
- Java 17
- Maven 3.8+
- IntelliJ IDEA или другая IDE

## Сборка и запуск приложения

### 1. Клонирование репозитория
Склонируйте проект на локальную машину и перейдите в папку проекта:
```bash
git clone https://github.com/kairat301004/task-time-tracker.git
cd task-time-tracker
```
### 2. Запуск базы данных PostgreSQL через Docker Compose

В корне проекта выполните команду:
```bash
docker-compose up -d
```
### 3. Сборка проекта
```bash
mvn clean package
```
### 4. Запуск приложения
```bash
mvn spring-boot:run
```
Или через саму IDE

Приложение будет доступно по адресу: http://localhost:8080

## Проверка работоспособности

### Swagger документация

Доступно в браузере по адресу:
http://localhost:8080/swagger-ui.html

### Postman коллекция

Для доступа к API необходимо сначала получить JWT токен. Отправьте POST запрос:

POST http://localhost:8080/auth/login

body: json
```json
{
"username": "admin",
"password": "admin123"
}
```
Скопируйте полученный токен. Для всех последующих запросов добавляйте заголовок:

Authorization: Bearer Token <ваш-токен>

### Тестовые запросы

1. Создание задачи

Запрос:

POST http://localhost:8080/api/tasks

Authorization: Bearer Token <токен>

body: json
```json
{
    "title": "Разработать REST API",
    "description": "Создать эндпоинты для учета времени"
}
```
Ответ будет в формате:
```json
{
    "id": 1,
    "title": "Разработать REST API",
    "description": "Создать эндпоинты для учета времени",
    "status": "NEW",
    "createdAt": "2026-04-14T10:30:00",
    "updatedAt": "2026-04-14T10:30:00"
}
```
2. Получение задачи по ID

Запрос:

GET http://localhost:8080/api/tasks/1

Authorization: Bearer Token <токен> (на последующие запросы он будет автоматически вставляться, но проверка на всякий случай)

3. Изменение статуса задачи

Запрос:

PATCH http://localhost:8080/api/tasks/1/status?status=IN_PROGRESS

Authorization: Bearer Token <токен>

4. Создание записи о затраченном времени

Запрос:

POST http://localhost:8080/api/time-records

Authorization: Bearer Token <токен>

body: json
```json
{
    "employeeId": 1001,
    "taskId": 1,
    "startTime": "2026-04-14T09:00:00",
    "endTime": "2026-04-14T17:00:00",
    "description": "Разработка REST эндпоинтов"
}
```

5. Получение информации о затраченном времени сотрудника за период

Запрос:

GET http://localhost:8080/api/time-records/employee/1001?start=2026-04-01T00:00:00&end=2026-04-30T23:59:59

Authorization: Bearer Token <токен>

Ответ будет в формате:
```json
[
    {
        "id": 1,
        "employeeId": 1001,
        "taskId": 1,
        "startTime": "2026-04-14T09:00:00",
        "endTime": "2026-04-14T17:00:00",
        "description": "Разработка REST эндпоинтов",
        "durationMinutes": 480,
        "durationFormatted": "8 ч. 0 мин."
    }
]
```
### Запуск тестов

Для запуска тестов выполните:

```bash
mvn test
```

### Остановка приложения и БД

Для остановки приложения нажмите Ctrl+C в терминале.

Для остановки и удаления контейнера PostgreSQL:
```bash
docker-compose down
```

Итог: реализован полный требуемый функционал тестового задания.
