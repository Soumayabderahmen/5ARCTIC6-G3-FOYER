pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = 'soumaya_github'
    }

    tools {
        maven 'Maven' // Utilisez la version de Maven configurée
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out the repository..."
                    git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER.git', branch: 'AbderahmenSoumaya-5ARCTIC6-G3', credentialsId: "${env.GITHUB_CREDENTIALS_ID}"
                }
            }
        }

        stage('Build and Package Application') {
            steps {
                echo 'Building the application...'
                withMaven(maven: 'Maven') {
                    sh 'mvn clean package' // Change this command as desired
                }
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                withMaven(maven: 'Maven') {
                    sh 'mvn test' // Exécute les tests unitaires
                }
            }
        }

        stage('Deploy to Docker') {
            steps {
                echo 'Deploying the application to Docker...'
                script {
                    // Assurez-vous que le fichier docker-compose.yml est présent
                    sh 'sudo docker-compose up -d' // Exécute la commande docker-compose
                }
            }
        }
    }

    post {
        success {
            echo 'The build was successful, the deliverable is available in the target directory.'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo 'The build failed. Check the logs for more details.'
        }
    }
}