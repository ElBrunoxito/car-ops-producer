# ETAPA 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
# Maven genera app.jar gracias a tu configuración de finalName
RUN mvn clean package -DskipTests

# ETAPA 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos directamente el archivo app.jar
COPY --from=build /app/target/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]