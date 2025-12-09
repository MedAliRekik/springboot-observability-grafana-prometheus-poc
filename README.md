# springboot-observability-grafana-prometheus-poc
exposer quelques endpoints, des métriques Prometheus, puis les visualiser dans Grafana.
# 🔍 Observability POC – Spring Boot + Prometheus + Grafana

## 🎯 Objectif

Mini projet monolithique qui montre comment monitorer une API Java (latence, nombre de requêtes, mémoire JVM)
avec Spring Boot Actuator, Micrometer, Prometheus et Grafana.

## 🧱 Stack technique

- Java 21 / Spring Boot 3
- Spring Web, Actuator, Micrometer (Prometheus)
- Prometheus
- Grafana
- Docker / Docker Compose

## 📐 Architecture

- 1 application monolithique Spring Boot (`/api/...`, `/actuator/prometheus`)
- Prometheus qui scrape l'application
- Grafana qui consomme Prometheus comme datasource pour afficher des dashboards

## 🚀 Démarrage rapide

```bash
git clone ...
cd observability-poc
docker-compose up --build
API : http://localhost:8080/api/hello

Prometheus : http://localhost:9090

Grafana : http://localhost:3000
 (login: admin / admin)

## 📊 Endpoints à tester

GET /api/hello → incrémente un compteur demo_hello_requests_total

GET /api/slow → simule un traitement lent (2s) et enregistre la durée

POST /api/memory?mb=50 → simule une consommation mémoire côté JVM

GET /actuator/prometheus → toutes les métriques (JVM + custom)

## 💡 Pistes d'amélioration

Ajouter des logs structurés (Loki)

Ajouter des traces distribuées (OpenTelemetry)

Ajouter des règles d'alerte Prometheus (latence, mémoire, etc.)
