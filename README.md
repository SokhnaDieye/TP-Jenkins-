# Student Backend - CI/CD avec Jenkins

## Description

Application Spring Boot de gestion d'étudiants avec pipeline CI/CD intégré utilisant Jenkins et Docker.

## Architecture CI/CD

![Pipeline Jenkins](/image/jenkins1.PNG)

Le pipeline Jenkins automatise les étapes suivantes :

1. **Build Maven** - Compilation du projet Spring Boot
2. **Tests unitaires** - Exécution des tests avec base H2 en mémoire
3. **Push Docker Hub** - Construction et publication de l'image Docker

## Statut du Pipeline

![Build Success](/image/jenkins2.PNG)

| Étape | Statut |
|-------|--------|
| Build Maven | ✅ Succès |
| Tests unitaires | ✅ Succès (1 test) |
| Push Docker Hub | ✅ Succès |

## 🐳 Image Docker

![Docker Hub](/image/DockerHub2.PNG)

L'image est disponible sur Docker Hub :

```bash
docker pull sokhnad/student-backend:latest