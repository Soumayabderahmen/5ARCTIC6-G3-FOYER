pipeline {
    agent { label 'Jenkins-Agent' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    environment {
        GITHUB_CREDENTIALS_ID = 'github-token'
        DOCKER_USER = 'mouhanedakermi'
        DOCKER_PASS = 'dockerhub'
        IMAGE_NAME = 'mouhanedakermi/foyer'
        RELEASE = 'v1.0.0'
    }

    stages {
        // Stage 1: Cleanup Workspace
        stage("Cleanup Workspace") {
            steps {
                cleanWs()
            }
        }

        // Stage 2: Checkout
        stage('Checkout') {
            steps {
                echo "Checking out the repository..."
                git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER', branch: 'main', credentialsId: 'github-token'
            }
        }

        // Stage 3: Nettoyage du projet
        stage('Nettoyage du projet') {
            steps {
                echo 'Nettoyage du projet...'
                sh 'mvn clean'
            }
        }

        // Stage 4: Création du livrable
        stage('Création du livrable') {
            steps {
                echo 'Création du livrable...'
                // Construire le livrable sans phase de test
                sh 'mvn package -DskipTests'
            }
        }
        stage('Building & Pushing Docker Image') {
            steps {
                script {
                    echo 'Building the image'
                    docker.withRegistry('',DOCKER_PASS) {
                        sh "docker build -t mouhanedakermi/foyer:${RELEASE}-${BUILD_NUMBER} ."
                    }
                    echo 'Pushing image to DockerHub'
                    docker.withRegistry('',DOCKER_PASS) {
                        sh "docker push mouhanedakermi/foyer:${RELEASE}-${BUILD_NUMBER}"
                    }
                }
            }
        }
    }

    post {
        always {
            emailext (
                subject: "Pipeline Status: ${BUILD_NUMBER}",
                body: '''<html>
                            <body>
                                <p>Build Status: ${BUILD_STATUS}</p>
                                <p>Build Number: ${BUILD_NUMBER}</p>
                                <p>Check the <a href="${BUILD_URL}">Console Output</a>.</p>
                            </body>
                         </html>''',
                to:'mouhanedakermi@gmail.com',
                from:'jenkins@example.com',
                replyTo:'jenkins@example.com',
                mimeType: 'text/html'
            )
        }
    }
}
