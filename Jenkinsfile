pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v $HOME/.m2:/root/.m2:z -u root'
            reuseNode true
        }
    }

    environment {
        GOOGLE_APPLICATION_CREDENTIALS = credentials('jenkins-agent-service-account')
    }

    stages {
        stage('Setup') {
            steps {
                env.VERSION = sh(script: "mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec", returnStdout: true)
            }
        }

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

        stage('Deploy') {
            steps {
                sh "mvn deploy:deploy-file -Dfile=kcbq-connector/target/components/packages/wepay-kafka-connect-bigquery-${VERSION}.zip -Durl=gs://apc-maven-artifacts/release -DgroupId=apc.kafka -DartifactId=kafka-bq-connector -Dversion=${VERSION}"
            }
        }
    }
}
