# Imagen Modelo
FROM eclipse-temurin:25-jdk
# Informar Puerto
EXPOSE 8080
# Dir Raiz de la app
WORKDIR /app
# Copiar Archivos dentro del contenedor
COPY ./pom.xml /app
COPY ./.mvn /app/.mvn
COPY ./mvnw /app
# Descargar dependencias
RUN ./mvnw dependency:go-offline
# Copiar Codigo fuente dentro del contenedor
COPY ./src /app/src
# Construir la aplicacion
RUN ./mvnw clean install -DskipTests
# Levantar app cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "/app/target/movies-0.0.1-SNAPSHOT.jar"]