FROM eclipse-temurin:21-jdk-slim

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR construido al contenedor
ARG JAR_FILE=target/BackEnd-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
