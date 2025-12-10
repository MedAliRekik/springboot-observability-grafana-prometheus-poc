# 🎯 Présentation du projet
POC d'observability d'une API monolithique Spring Boot (Java 21) instrumentée avec Micrometer pour être monitorée par Prometheus et visualisée dans Grafana.

Objectif : exposer plusieurs endpoints de démonstration et observer leurs métriques (latence, compteurs, consommation mémoire JVM, etc.) via `/actuator/prometheus`, scrappé par Prometheus et affiché dans Grafana.

## 🧱 Stack technique
- **Java 21 / Spring Boot 3** : API REST monolithique.
- **Spring Boot Actuator** : endpoints health/info/métriques dont `/actuator/prometheus`.
- **Micrometer + Prometheus Registry** : instrumentation applicative et export des métriques vers Prometheus.
- **Prometheus** : collecte et stockage des métriques exposées par l'application.
- **Grafana** : visualisation des métriques via dashboards basés sur la datasource Prometheus.
- **Docker & Docker Compose** : orchestration locale de l'application, Prometheus et Grafana.

## 📐 Architecture
- 1 application Spring Boot expose des endpoints REST (`/api/...`) et des métriques via `/actuator/prometheus`.
- Prometheus scrappe périodiquement l'endpoint `/actuator/prometheus` de l'application.
- Grafana consomme Prometheus comme datasource pour construire des dashboards.

```
[Client HTTP]
     |
     v
[Spring Boot API] --expose--> /actuator/prometheus --scrape--> [Prometheus] --datasource--> [Grafana UI]
```

## 📂 Structure du projet
- `src/main/java/com/alirekik/observability_poc/ObservabilityPocApplication.java` : classe de démarrage Spring Boot.
- `src/main/java/com/alirekik/observability_poc/controller/DemoController.java` : expose les endpoints REST de démonstration (`/api`).
- `src/main/java/com/alirekik/observability_poc/service/SlowService.java` : simule un traitement lent.
- `src/main/java/com/alirekik/observability_poc/service/MemoryService.java` : simule une consommation mémoire (fuite contrôlée).
- `src/main/java/com/alirekik/observability_poc/monitoring/ApiMetrics.java` : déclare des métriques personnalisées Micrometer (compteur d'appels / timer de latence).
- `src/main/resources/application.properties` : configuration Actuator (exposition des endpoints), port et tags de métriques.
- `pom.xml` : dépendances (Spring Web, Actuator, Micrometer Prometheus, tests) et configuration Maven.
- `Dockerfile` : build multi-étapes (Maven puis image JRE 21) pour packager l'application.
- `docker-compose.yml` : lance l'application, Prometheus et Grafana.
- `prometheus.yml` : configuration de scrape de Prometheus sur `app:8080/actuator/prometheus`.

## 🚀 Lancer le projet en local (sans Docker)
### Prérequis
- Java 21
- Maven 3.x

### Commandes
```bash
mvn clean package
mvn spring-boot:run
```

### URLs
- API : `http://localhost:8080/api/hello`
- Requête lente : `http://localhost:8080/api/slow`
- Métriques Prometheus : `http://localhost:8080/actuator/prometheus`

## 🐳 Lancer le projet avec Docker / Docker Compose
### Prérequis
- Docker / Docker Desktop installé

### Commandes
```bash
docker compose up --build
```
Arrêter les conteneurs :
```bash
docker compose down
```

### URLs
- API Spring Boot : `http://localhost:8080`
- Métriques : `http://localhost:8080/actuator/prometheus`
- Prometheus UI : `http://localhost:9090`
- Grafana UI : `http://localhost:3000`

## 📊 Utiliser Prometheus
1. Ouvrir `http://localhost:9090`.
2. Vérifier les cibles dans **Status > Targets** : la cible `spring-app` doit être `UP` (scrape de `app:8080/actuator/prometheus`).
3. Dans **Graph**, tester des requêtes PromQL :
   - `up` pour vérifier l'état de la cible.
   - `jvm_memory_used_bytes` pour la mémoire JVM.
   - `demo_hello_requests_total` (compteur custom).
   - `demo_slow_request_duration_count` / `_sum` pour la latence agrégée du timer.

## 📈 Utiliser Grafana
1. Ouvrir `http://localhost:3000` (login par défaut Grafana OSS : `admin` / `admin`, puis changer le mot de passe si demandé).
2. Ajouter une datasource **Prometheus** avec l'URL interne `http://prometheus:9090` (dans le réseau Docker).
3. Créer un dashboard puis un panel :
   - Exemple de requête : `rate(demo_hello_requests_total[1m])` pour le rythme d'appels à `/api/hello`.
   - Exemple de latence : `histogram_quantile(0.95, rate(demo_slow_request_duration_bucket[5m]))` ou `rate(demo_slow_request_duration_sum[1m])` / `rate(demo_slow_request_duration_count[1m])`.
   - Ajouter des panels sur les métriques JVM (`jvm_memory_used_bytes`, `process_cpu_usage`, etc.).

## 🔗 Endpoints disponibles
- `GET /api/hello` → endpoint rapide qui incrémente le compteur `demo_hello_requests_total`.
- `GET /api/slow` → simule une requête lente (~2s) et enregistre la durée via le timer `demo_slow_request_duration`.
- `POST /api/memory?mb=<taille>` → alloue `<taille>` MB en mémoire pour simuler une fuite et suivre la consommation JVM.
- `GET /api/memory/chunks` → retourne le nombre de blocs mémoire alloués.
- `GET /actuator/prometheus` → export complet des métriques (JVM + métriques custom).

## 🔮 Pistes d'amélioration
- Ajouter des logs structurés (ex. stack Loki + Promtail).
- Intégrer la traçabilité distribuée (OpenTelemetry, exporter OTLP vers Grafana Tempo ou Jaeger).
- Définir des règles d'alerting Prometheus (latence, erreurs HTTP, mémoire, etc.).
- Fournir des dashboards Grafana préconfigurés pour l'API et la JVM.
