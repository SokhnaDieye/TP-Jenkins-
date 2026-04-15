pipeline {
    agent none

    environment {
        IMAGE_NAME = "sokhnad/student-backend"
    }

    stages {
        stage('Build & Test Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17-alpine'
                    args '-u root -v $HOME/.m2:/root/.m2'
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

        stage('Push to Docker Hub') {
            agent {
                docker {
                    image 'docker:25.0.3'
                    args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
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
                    sh 'echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin'
                    sh "docker build -t ${IMAGE_NAME}:v${BUILD_NUMBER} -t ${IMAGE_NAME}:latest ."
                    sh "docker push ${IMAGE_NAME}:v${BUILD_NUMBER}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline réussi ! Image disponible : ${IMAGE_NAME}:v${BUILD_NUMBER}"
        }
        failure {
            echo 'Pipeline échoué. Consulte les logs ci-dessus.'
        }
        always {
            cleanWs()
            echo 'Fin du pipeline.'
        }
    }
}