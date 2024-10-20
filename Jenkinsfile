pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = 'githubT'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out the repository..."
                    git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER.git', branch: 'RimBensalah-5ARCTIC6-G3', credentialsId: "${env.GITHUB_CREDENTIALS_ID}"
                }
            }
        }

        stage('Nettoyage du projet') {
            steps {
                echo 'Nettoyage du projet...'
                sh 'mvn clean'
            }
        }

        stage('Exécution des tests') {
                    steps {
                        echo 'Exécution des tests...'
                        // Exécuter les tests JUnit
                        sh 'mvn test'
                    }
                    post {
                        always {
                            // Publier les résultats des tests
                            junit '**/target/surefire-reports/*.xml'
                        }
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
        success {
            echo 'La construction a réussi, le livrable est disponible dans le dossier target.'
        }
        failure {
            echo 'La construction a échoué. Vérifiez les logs pour plus de détails.'
        }
    }
    //test2
}
