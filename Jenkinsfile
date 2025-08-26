pipeline {
    agent any
    environment {
        STAGING_SERVER = 'lenovo@192.168.1.231'
        ARTIFACT_NAME = 'demo-0.0.1-SNAPSHOT.jar'
        DEPLOY_PATH = '/home/lenovo/staging/'
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
                sshagent(['ubuntu-staging-key']) {
                    sh '''
                        # Copiar archivo JAR
                        scp target/${ARTIFACT_NAME} ${STAGING_SERVER}:${DEPLOY_PATH}

                        # Conectarse por SSH y ejecutar despliegue
                        ssh ${STAGING_SERVER} '
                            . /root/.bashrc
                            echo "Stopping any process on port 8080..."
                            fuser -k 8080/tcp || true

                            echo "Starting application with nohup..."
                            nohup /opt/jdk-24.0.2/bin/java -jar ${DEPLOY_PATH}${ARTIFACT_NAME} \
                                --server.port=8080 \
                                --server.address=0.0.0.0 \
                                > ${DEPLOY_PATH}nohup.log 2>&1 < /dev/null &
                        '
                    '''
                }
            }
        }

        stage('Validate Deployment') {
            steps {
                sshagent(['ubuntu-staging-key']) {
                    sh '''
                    . /root/.bashrc
                    echo "Waiting for application to start..."
                    for i in $(seq 1 12); do
                        curl --fail http://192.168.1.231:8080/health && exit 0
                        echo "App not ready yet ($i/12), retrying in 5s..."
                        sleep 5
                    done
                    echo "Application did not start in time"
                    exit 1
                    '''
                }
            }
        }
    }
}
