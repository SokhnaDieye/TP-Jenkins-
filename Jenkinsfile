pipeline {
    agent none

    environment {
        IMAGE_NAME     = "sokhnad/student-backend"
        CONTAINER_NAME = "student-backend"
        APP_PORT       = "8080"
        REMOTE_HOST    = "192.168.56.20" 
    }

    stages {

        // ── STAGE 1 : Build Maven + Tests ───────────────────────────────
        stage('Build & Test Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17-alpine'
                    args '-v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                echo 'Build et tests du projet Spring Boot...'
                sh 'mvn clean package'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
                success {
                    stash name: 'jar-file', includes: 'target/*.jar'
                }
            }
        }

        // ── STAGE 2 : Build & Push Docker Hub ────────────────────────────
        stage('Push to Docker Hub') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                unstash 'jar-file'
                
                echo 'Build et push de l\'image Docker...'
                withCredentials([usernamePassword(
                    credentialsId   : 'dockerhub_credentials',
                    usernameVariable: 'DOCKER_HUB_USERNAME',
                    passwordVariable: 'DOCKER_HUB_PASSWORD'
                )]) {
                    sh 'docker login -u $DOCKER_HUB_USERNAME -p $DOCKER_HUB_PASSWORD'
                    sh "docker build -t ${IMAGE_NAME}:v${BUILD_NUMBER} -t ${IMAGE_NAME}:latest ."
                    sh "docker push ${IMAGE_NAME}:v${BUILD_NUMBER}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        // ── STAGE 3 : Déploiement local avec Docker ──────────────────────
        stage('Deploy Local') {
            agent any
            steps {
                echo 'Déploiement local de l\'application...'
                sh """
                    echo "=== Arrêt et suppression de l\'ancien conteneur ==="
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true

                    echo "=== Lancement du nouveau conteneur ==="
                    docker run -d \\
                        --name ${CONTAINER_NAME} \\
                        --restart unless-stopped \\
                        -p ${APP_PORT}:${APP_PORT} \\
                        ${IMAGE_NAME}:v${BUILD_NUMBER}

                    echo "=== Nettoyage des anciennes images ==="
                    docker image prune -f
                """
            }
        }
    }

    // ── POST ──────────────────────────────────────────────────────────────
    post {
        success {
            echo "Pipeline réussi ! Image déployée : ${IMAGE_NAME}:v${BUILD_NUMBER}"
            echo "Application disponible sur http://localhost:${APP_PORT}"
        }
        failure {
            echo 'Pipeline échoué. Consulte les logs ci-dessus.'
        }
        always {
            echo 'Fin du pipeline.'
        }
    }
}