# student-backend — TP3 DevOps

Backend Spring Boot pour la gestion des étudiants.

## Prérequis
- Java 17
- Maven
- MySQL accessible sur server-dba (192.168.56.30)

## Lancer en local (dev)
```bash
mvn spring-boot:run
```

## Builder le JAR
```bash
mvn clean package -DskipTests
# Le JAR se trouve dans : target/student-backend-0.0.1-SNAPSHOT.jar
```

## Déploiement sur server-back (Vagrant)
```bash
vagrant upload student-backend/target/student-backend-0.0.1-SNAPSHOT.jar /home/vagrant/ server-back
# Se connecter et lancer :
vagrant ssh server-back
java -jar /home/vagrant/student-backend-0.0.1-SNAPSHOT.jar &
```

## Endpoints disponibles
| Méthode | URL                    | Description              |
|---------|------------------------|--------------------------|
| GET     | /api/students          | Lister tous les étudiants|
| GET     | /api/students/{id}     | Récupérer un étudiant    |
| POST    | /api/students          | Créer un étudiant        |
| PUT     | /api/students/{id}     | Modifier un étudiant     |
| DELETE  | /api/students/{id}     | Supprimer un étudiant    |

## Test rapide avec curl
```bash
# Lister tous les étudiants
curl http://192.168.56.20:8080/api/students

# Créer un étudiant
curl -X POST http://192.168.56.20:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@esp.sn","filiere":"GL"}'
```
