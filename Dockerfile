# Primera etapa: Compilar la aplicación
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
# Intentar descargar dependencias con reintentos
RUN mvn -B dependency:resolve-plugins dependency:resolve -DskipTests
COPY src ./src
# Compilar y empaquetar la aplicación
RUN mvn -B package -DskipTests

# Segunda etapa: Ejecutar la aplicación
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiar el JAR desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar
# Exponer el puerto
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]