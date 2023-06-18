pipeline {
    agent any

    tools {
        maven "maven"
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test jacoco:report'
            }
            post {
                always {
                    junit '/target/surefire-reports/*.xml'
                }
            }
        }


        stage('Sonarqube') {
            environment {
                scannerHome = tool 'SonarQubeScanner'
            }
            steps {
                withSonarQubeEnv('sonarcube') {
                    bat 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy') {
            steps {
                bat('xcopy ".\\target\\esm.war" "C:\\Tomcat 10.1\\webapps" /Y')
            }
        }
    }
}