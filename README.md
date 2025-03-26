# Сервис для расчета скидок

Данный REST API сервис вычисляет итоговую стоимость товара с учетом скидки на основе переданных параметров. Если для переданных параметров скидка не найжена, то возвращается исходная стоимость продукта.
### Список эндпоинтов
- `/discount/fixed`: вычисляет фиксированную скидку.
- `/discount/variable`: вычисляет переменную скидку.

Все эндпоинты возвращают итоговую стоимость и принимают следующие параметры:
- `price`: начальная стоимость продукта, для которого рассчитывается скидка.
- `product_category`: ID категории продукта.
- `client_category`: ID категории клиента.

Данные параметры являются **обязательными**.

### Конфигурация и запуск

#### Предварительные требования для запуска
- Java 17+
- Gradle 7.x+

#### Конфигурация Docker Compose
Основные переменные окружения можно задать в `docker-compose.yaml`
```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://database:3306/discount_service
  SPRING_DATASOURCE_USERNAME: mysql
  SPRING_DATASOURCE_PASSWORD: mysql
```

#### Сборка приложения
1. Соберите JAR-файл с помощью Gradle:
```bash
./gradlew bootJar
```
Собранный JAR будет находиться в `build/libs/discount-service.jar`

2. Соберите Docker-образ:
```bash
docker build -t discount-service .
```

3. Запустите Docker Compose:
```bash
docker-compose up
```
