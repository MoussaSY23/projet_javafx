# Utiliser une image de base avec Java
FROM openjdk:23-jdk-slim

# Installer les dépendances nécessaires pour JavaFX (si nécessaire)
RUN apt-get update && apt-get install -y libgtk-3-0 libx11-xcb1 libxtst6 libxrandr2 libasound2 libfreetype6 libgl1 libxi6

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR dans le conteneur
COPY target/projet_exam.jar /app/projet_exam.jar

# Exposer le port (si besoin)
EXPOSE 8080

# Démarrer l'application
CMD ["java", "-jar", "/app/projet_exam.jar"]
