pipeline {
    agent none

    environment {
        IMAGE_NAME = "sokhnad/student-backend"
        CONTAINER_NAME = "student-backend"
        APP_PORT = "8080"
    }

    stages {

        // ── STAGE 1 : Build Maven ─────────────────────────────────────────
        stage('Build Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17-alpine'
                    args '-u root -v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                echo '📦 Build du projet Spring Boot...'
                sh "mvn clean package -DskipTests"
            }
        }

        // ── STAGE 2 : Tests unitaires ─────────────────────────────────────
        stage('Unit Test') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17-alpine'
                    args '-u root -v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                echo '🧪 Exécution des tests unitaires...'
                sh "mvn test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        // ── STAGE 3 : Build & Push Docker Hub ────────────────────────────
        stage('Push to Docker Hub') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                echo '🐳 Build et push de l\'image Docker...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub_credentials',
                    passwordVariable: 'DOCKER_HUB_PASSWORD',
                    usernameVariable: 'DOCKER_HUB_USERNAME'
                )]) {
                    sh "docker login -u $DOCKER_HUB_USERNAME -p $DOCKER_HUB_PASSWORD"
                    sh "docker build -t ${IMAGE_NAME}:v${BUILD_NUMBER} ."
                    sh "docker push ${IMAGE_NAME}:v${BUILD_NUMBER}"
                }
            }
        }

        // ── STAGE 4 : Déploiement sur serveur distant ─────────────────────
        stage('Deploy on Remote Server') {
            agent any
            steps {
                echo ' Déploiement sur le serveur distant...'
                sh """
                    ssh -i /idrsa -o StrictHostKeyChecking=no user@remote.server.com '
                    docker pull ${IMAGE_NAME}:v${BUILD_NUMBER} &&
                    docker stop ${CONTAINER_NAME} || true &&
                    docker rm   ${CONTAINER_NAME} || true &&
                    docker run -d \\
                        --name ${CONTAINER_NAME} \\
                        -p ${APP_PORT}:${APP_PORT} \\
                        ${IMAGE_NAME}:v${BUILD_NUMBER}
                    '
                """
            }
        }

    }

    // ── POST ──────────────────────────────────────────────────────────────
    post {
        success {
            echo 'Pipeline exécuté avec succès !'
        }
        failure {
            echo 'Pipeline échoué.'
        }
    }
}
