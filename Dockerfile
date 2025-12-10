# Étape 1 : build avec Maven dans un conteneur
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copier tout le projet dans l'image
COPY . .

# Construire le jar (sans lancer les tests)
RUN mvn -q -DskipTests package

# Étape 2 : image finale pour exécuter le jar
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copier le jar généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080 dans le conteneur
EXPOSE 8080

# Commande de démarrage du conteneur
ENTRYPOINT ["java", "-jar", "app.jar"]
