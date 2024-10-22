FROM openjdk:17-slim
WORKDIR /app
COPY target/Foyer.jar .
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "Foyer.jar"]