# Étape 1: Utiliser une image de base Maven avec OpenJDK 17 pour construire l'application
FROM maven:3.8.1-openjdk-17 AS build

# Définit le répertoire de travail à /app
WORKDIR /app

# Copie le fichier pom.xml dans le répertoire de travail
COPY pom.xml .

# Copie le répertoire src (qui contient le code source de l'application) dans le répertoire de travail
COPY src ./src

# Exécute Maven pour nettoyer et construire le projet, en sautant les tests
RUN mvn clean package -DskipTests

# Étape 2: Utiliser une image de base OpenJDK 17 slim pour l'exécution de l'application
FROM openjdk:17-jdk-slim

# Définit à nouveau le répertoire de travail à /app
WORKDIR /app

# Copie le fichier JAR construit depuis l'étape précédente dans le répertoire de travail de l'image d'exécution
COPY --from=build /app/target/*.jar /app/app.jar

# Expose le port 8080 pour que l'application soit accessible depuis l'extérieur du conteneur
EXPOSE 8080

# Définit la commande d'entrée qui sera exécutée lorsque le conteneur sera lancé
# Cela exécute l'application Java à partir du fichier app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
