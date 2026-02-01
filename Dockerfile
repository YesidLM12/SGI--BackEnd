FROM ubuntu:latest
LABEL authors="Yesid"

# Imagen base con Java
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY target/SGI-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "SGI-0.0.1-SNAPSHOT.jar"]