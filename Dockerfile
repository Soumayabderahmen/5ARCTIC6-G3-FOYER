# Étape pour exécuter l'application
FROM openjdk:17-jdk

# Spécifiez le répertoire de travail
WORKDIR /app

# Copier le fichier JAR produit de l'étape de construction
COPY --from=build /app/target/Foyer.jar .

# Exposer le port 8081
EXPOSE 8081

# Point d'entrée de l'application
ENTRYPOINT ["java", "-jar", "Foyer.jar"]