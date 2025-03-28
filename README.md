![Build](https://github.com/central-university-dev/backend-academy-2025-spring-template/actions/workflows/build.yaml/badge.svg)

# Link Tracker

<!-- этот файл можно и нужно менять -->

Проект сделан в рамках курса Академия Бэкенда.

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 23` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:

* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.

Для дополнительной справки: [HELP.md](./HELP.md)

# Запуск приложения

- Создайте файл .env в корне проекта Bot и добавьте следующие переменные: TELEGRAM_TOKEN
- Создайте файл .env в корне проекта Scrapper и добавьте следующие переменные: GITHUB_TOKEN, SO_TOKEN_KEY, SO_ACCESS_TOKEN

mvn clean install

java -jar scrapper/target/scrapper-1.0.jar

java -jar bot/target/bot-1.0.jar
