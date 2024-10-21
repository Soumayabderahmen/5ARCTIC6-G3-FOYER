pipeline {
    agent { label 'Jenkins-Agent'}
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    environment {
        GITHUB_CREDENTIALS_ID = 'github-token'
    }

    stages {
        stage("Cleanup Workspace"){
                steps{
                cleanWs()
                }
        }
    }

    stages {
        stage('Checkout') {
            steps {
                    echo "Checking out the repository..."
                    git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER', branch: 'main', credentialsId: 'github-token'
                }
            }
        }

        stage('Nettoyage du projet') {
            steps {
                echo 'Nettoyage du projet...'
                sh 'mvn clean'
            }
        }

        stage('Création du livrable') {
            steps {
                echo 'Création du livrable...'
                // Construire le livrable sans phase de test
                sh 'mvn package -DskipTests'
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
                        <body>
                     </html>'''
            to:'mouhanedakermi@gmail.com',
            from:'jenkins@example.com',
            replyTo:'jenkins@example.com',
            mimeType: 'text/html'
        )
        }
    }
}