pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "noormezgar/partner-contract-service"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/Noormezgar/pi_devops.git'
            }
        }

        stage('Build Maven') {
            steps {
                dir('back/business-domain/partner-contract-service') {
                    sh 'chmod +x mvnw || true'
                    sh './mvnw clean package -DskipTests || mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('back/business-domain/partner-contract-service') {
                    sh 'docker build -t noormezgar/partner-contract-service:latest .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'bb7b71c0-fcae-4356-aaea-dcc364177a01',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {

                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

                    sh 'docker push noormezgar/partner-contract-service:latest'
                }
            }
        }
    }

    post {
        success {
            echo 'Docker image pushed successfully!'
        }

        failure {
            echo 'Pipeline failed!'
        }
    }
}