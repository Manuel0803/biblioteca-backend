# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen de Maven con Java 17 para compilar nuestro proyecto.
# Cambia "17" si usas otra versión de Java (ej: 11).
FROM maven:3.9.5-eclipse-temurin-17-focal AS build

# Establecemos el directorio de trabajo dentro de la imagen
WORKDIR /app

# Copiamos solo el pom.xml para aprovechar el cache de Docker
# Si las dependencias no cambian, Docker no las volverá a descargar
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos la aplicación y creamos el .jar, saltando los tests para velocidad
RUN mvn package -DskipTests


# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen de Java mucho más ligera solo para ejecutar la app
FROM openjdk:17-jdk-slim

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos SOLAMENTE el .jar compilado desde la etapa de 'build'
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que corre nuestra app Spring Boot (usualmente 8080)
EXPOSE 8080

# El comando que se ejecutará cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]