# Utilisation de l'image OpenJDK
FROM openjdk:23-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré dans le conteneur
COPY target/*.jar app.jar

# Définir la commande d'exécution
ENTRYPOINT ["java", "-jar", "app.jar"]
