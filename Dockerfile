FROM eclipse-temurin:latest
LABEL authors="izzatbek"
WORKDIR /app


ENTRYPOINT ["java", "-jar", "app.jar"]