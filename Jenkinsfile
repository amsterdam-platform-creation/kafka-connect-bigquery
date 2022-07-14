pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-u root'
        }
    }

    environment {
        GOOGLE_APPLICATION_CREDENTIALS = credentials('jenkins-agent-service-account')
    }

    stages {

        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'kcbq-connector/target/surefire-reports/*.xml'
                }
            }
        }
    }
}
