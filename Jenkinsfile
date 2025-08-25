pipeline {
    agent any
    environment {
        STAGING_SERVER = 'lenovo@192.168.1.231'
        ARTIFACT_NAME = 'demo-0.0.1-SNAPSHOT.jar'
        DEPLOY_PATH = '/home/user/staging/'
        HEALTH_URL = 'http://192.168.1.231:8080/health'
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/ezbrush/springboot-staging.git'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
        stage('Code Quality') {
            steps {
                sh 'mvn checkstyle:check'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Deploy to Staging') {
            steps {
                sh "scp target/${ARTIFACT_NAME} ${STAGING_SERVER}:${DEPLOY_PATH}"
                sh """
                    ssh ${STAGING_SERVER} '
                        fuser -k 8080/tcp || true
                        nohup java -jar ${DEPLOY_PATH}${ARTIFACT_NAME} > ${DEPLOY_PATH}nohup.out 2>&1 &
                    '
                """
            }
        }
        stage('Validate Deployment') {
            steps {
                sh 'sleep 30'
                sh "curl --fail ${HEALTH_URL}"
            }
        }
    }
}