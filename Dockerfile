# Étape pour exécuter l'application
FROM openjdk:17-jdk

# Spécifiez le répertoire de travail
WORKDIR /app

# Copier le fichier JAR produit de l'étape de construction
COPY target/Foyer-1.0-SNAPSHOT.jar app.jar


# Exposer le port 8081
EXPOSE 8081

# Point d'entrée de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]