pipeline {
    agent none

    environment {
        IMAGE_NAME     = "sokhnad/student-backend"
        CONTAINER_NAME = "student-backend"
        APP_PORT       = "8080"
        REMOTE_HOST    = "192.168.56.20" 
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
                echo 'Build du projet Spring Boot...'
                sh 'mvn clean package -DskipTests'
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
                sh 'mvn test'
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

    }

    // ── POST ──────────────────────────────────────────────────────────────
    post {
        success {
            echo "Pipeline réussi !"
        }
        failure {
            echo ' Pipeline échoué. Consulte les logs ci-dessus.'
        }
        always {
            echo 'Fin du pipeline.'
        }
    }
}