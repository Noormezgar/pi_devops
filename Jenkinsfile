pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token') // Ensure this is configured in Jenkins
        FRONTEND_DIR = 'front'
    }

    tools {
        jdk 'Java17' // Name configured in Jenkins Global Tool Configuration
        maven 'Maven3' // Name configured in Jenkins Global Tool Configuration
        nodejs 'Node20' // Name configured in Jenkins Global Tool Configuration
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Business Service Tests + Sonar') {
            steps {
                bat 'cd back\\business-domain\\microservices-project\\business-service && mvn -B test'
                bat 'cd back\\business-domain\\microservices-project\\business-service && mvn -B clean verify sonar:sonar -Dsonar.projectKey=business-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
            }
        }

        stage('Partner Performance Tests + Sonar') {
            steps {
                bat 'cd back\\business-domain\\partner-performance-service && mvn -B test'
                bat 'cd back\\business-domain\\partner-performance-service && mvn -B clean verify sonar:sonar -Dsonar.projectKey=partner-performance-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
            }
        }

        stage('Partner Contract Tests + Sonar') {
            steps {
                bat 'cd back\\business-domain\\partner-contract-service && mvn -B test'
                bat 'cd back\\business-domain\\partner-contract-service && mvn -B clean verify sonar:sonar -Dsonar.projectKey=partner-contract-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
            }
        }

        stage('Partner Billing Tests + Sonar') {
            steps {
                bat 'cd back\\business-domain\\partner-billing-service && mvn -B test'
                bat 'cd back\\business-domain\\partner-billing-service && mvn -B clean verify sonar:sonar -Dsonar.projectKey=partner-billing-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
            }
        }

        stage('Partner Intelligence Tests + Sonar') {
            steps {
                bat 'cd back\\business-domain\\partner-intelligence-service && mvn -B test'
                bat 'cd back\\business-domain\\partner-intelligence-service && mvn -B clean verify sonar:sonar -Dsonar.projectKey=partner-intelligence-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
            }
        }

        stage('Voucher Fraud Tests + Sonar') {
            steps {
                bat 'cd back\\business-domain\\voucher-fraud-service && mvn -B test'
                bat 'cd back\\business-domain\\voucher-fraud-service && mvn -B clean verify sonar:sonar -Dsonar.projectKey=voucher-fraud-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
            }
        }

        stage('Frontend Install') {
            steps {
                dir("${FRONTEND_DIR}") {
                    bat 'npm ci'
                }
            }
        }

        stage('Frontend Angular Tests') {
            steps {
                dir("${FRONTEND_DIR}") {
                    bat 'npx ng test --watch=false --browsers=ChromeHeadless --code-coverage'
                }
            }
        }

        stage('Frontend Build') {
            steps {
                dir("${FRONTEND_DIR}") {
                    bat 'npx ng build'
                }
            }
        }

       stage('Frontend Sonar Scan') {
    steps {
        dir("${FRONTEND_DIR}") {
            bat 'C:\\sonar-scanner\\sonar-scanner-8.0.1.6346-windows-x64\\bin\\sonar-scanner.bat -Dsonar.projectKey=forme-frontend -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_TOKEN%'
        }
    }
}
    }

    post {
        always {
            echo 'Pipeline Execution Completed.'
        }
        success {
            echo 'Final Summary: All stages completed successfully.'
        }
        failure {
            echo 'Final Summary: Pipeline failed. Please check the logs.'
        }
    }
}
