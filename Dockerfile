# Étape de construction
FROM openjdk:17-jdk as build

# Spécifiez le répertoire de travail
WORKDIR /app

# Copier le fichier JAR dans le conteneur
COPY target/Foyer.jar .

# Exposer le port 8081
EXPOSE 8081

# Point d'entrée de l'application
ENTRYPOINT ["java", "-jar", "Foyer.jar"]




