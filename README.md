[![Build Status](https://travis-ci.org/zagamaza/sub-learn-telegram.svg?branch=develop)](https://travis-ci.org/zagamaza/sub-learn-telegram)
# Сервис sub-learn-telegram

## Установка, настройка и запуск

### Зависимости

* Java OpenJDK 11
* SpringBoot 2.1.6.RELEASE
* Telegram Bot API 4.3.1
* Redis
* Gradle 5.4.1
* Lombok 1.18.6
* Spring Cloud 2.1.2.RELEASE
* Google JSON 2.8.2

### Параметры приложения

Все конфигурации проекта лежат в файле
```
application.yaml
```

### Сборка приложения

Проект собирается gradle’ом из корневой папки при помощи скрипта:

```
build.sh  - Linux
build.bat - Windows
```

### Запуск приложения

Запустить приложение можно выполнив скрипт из корневой папки:
```
start.sh  - Linux 
start.bat - Windows
```
### Остановка приложения

Остановить приложение можно последовательно выполнив команды:
```
cd docker
docker-compose -f docker-compose-test.yaml down 
```

Для локального запуска БД можно последовательно выполнить команды:
```
cd docker
docker-compose -f docker-compose-dev.yaml up -d
```