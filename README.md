# Проект автотестов для AQA тестового задания

## Требования
- Java 17 или выше
- Maven 3.8+
- Тестируемое приложение (internal-0.0.1-SNAPSHOT.jar)

## Структура тестов
- `LoginTests` - тесты аутентификации
- `ActionTests` - тесты выполнения действий
- `LogoutTests` - тесты завершения сессии
- `SequenceTests` - сквозной сценарий
- `ApiKeyTests` - тесты Api-ключа

## Запуск

### 1. Запустите тестируемое приложение
```bash
java -jar -Dsecret=qazWSXedc -Dmock=http://localhost:8888 internal-0.0.1-SNAPSHOT.jar
```

### 2. Запустите тесты
```bash
mvn clean test
```

### 3. Сформируйте отчёт
```bash
mvn allure:serve
```
