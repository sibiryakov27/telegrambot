# Используем openjdk в качестве базового образа
FROM openjdk:11-jre-slim

# Установка каталога приложения
WORKDIR /app

# Скачивание jar файла из репозитория
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Установка переменной окружения
ARG BOT_TOKEN
ARG BOT_NAME
ARG BOT_CHATID
ENV BOT_TOKEN=$BOT_TOKEN
ENV BOT_NAME=$BOT_NAME
ENV BOT_CHATID=$BOT_CHATID
ENV TZ=Europe/Moscow

# Установка порта
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java","-jar","app.jar"]
