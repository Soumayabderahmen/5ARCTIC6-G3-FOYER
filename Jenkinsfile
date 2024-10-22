pipeline {
    agent { label 'Jenkins-Agent' }
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    environment {
        GITHUB_CREDENTIALS_ID = 'github-token'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub'
    }

    stages {
        // Stage 1: Checkout
        stage('Checkout') {
            steps {
                echo "Checking out the repository..."
                git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER', branch: 'main', credentialsId: 'github-token'
            }
        }

        // Stage 2: Nettoyage du projet
        stage('Nettoyage du projet') {
            steps {
                echo 'Nettoyage du projet...'
                sh 'mvn clean'
            }
        }

        // Stage 3: Création du livrable
        stage('Création du livrable') {
            steps {
                echo 'Création du livrable...'
                // Construire le livrable sans phase de test
                sh 'mvn package -DskipTests'
            }
        }
        stage ('Building docker image') {
            steps {
                dir('5ARCTIC6-G3-FOYER') {
                sh 'docker build -t mouhanedakermi/mouhanedakermi_g3_foyer:v1.0.0 .'
                }
            }
        }
        stage ('Pushing image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${env.DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                    sh 'docker push mouhanedakermi/mouhanedakermi_g3_foyer:v1.0.0'
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
