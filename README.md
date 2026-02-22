# Проект автотестов для AQA тестового задания

## Требования
- Java 17 или выше
- Maven 3.8+
- Тестируемое приложение (internal-0.0.1-SNAPSHOT.jar)

## Структура тестов
- `LoginTests` - тесты аутентификации
- `ActionTests` - тесты выполнения действий
- `LogoutTests` - тесты завершения сессии
- `NegativeTests` - негативные сценарии
- `BoundaryTests` - граничные значения
- `IntegrationTests` - комплексные сценарии
- `SecurityTests` - тесты безопасности

## Запуск

### 1. Запустите тестируемое приложение
```bash
java -jar -Dsecret=qazWSXedc -Dmock=http://localhost:8888 internal-0.0.1-SNAPSHOT.jar
