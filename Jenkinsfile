pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = 'devops'
        GITHUB_URL = 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER.git'
        BRANCH_NAME = 'RimBensalah-5ARCTIC6-G3'
        GIT_USERNAME = 'Rimbensalah11' // Nom d'utilisateur GitHub
        WORKSPACE_DIR = '/var/lib/jenkins/workspace/my-first-pipeline/5ARCTIC6-G3-FOYER' // Chemin vers le répertoire contenant le POM
        DOCKER_USERNAME = 'rimbs11'
        DOCKER_CREDENTIALS_ID = 'Docker' // Référence aux identifiants Docker stockés dans Jenkins
    }

    stages {

        // Étape de clonage du dépôt GitHub
        stage('Clone Repository') {
            steps {
                script {
                    echo "Clonage du dépôt GitHub..."
                    timeout(time: 10, unit: 'MINUTES') {
                        withCredentials([usernamePassword(credentialsId: GITHUB_CREDENTIALS_ID, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                            sh """
                                git config --global credential.helper store
                                echo "https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com" > ~/.git-credentials
                                git config --global http.postBuffer 1048576  # 1 Mo

                                if [ -d "${WORKSPACE_DIR}" ]; then
                                    echo "Suppression du dossier existant..."
                                    rm -rf ${WORKSPACE_DIR}
                                fi

                                git clone --single-branch -b ${BRANCH_NAME} ${GITHUB_URL} ${WORKSPACE_DIR}
                            """
                        }
                    }
                }
            }
        }

        // Vérification de l'existence du fichier pom.xml
        stage('Check POM') {
            steps {
                script {
                    if (!fileExists("${WORKSPACE_DIR}/pom.xml")) {
                        error('Le fichier pom.xml est introuvable. Veuillez vérifier le dépôt.')
                    }
                }
            }
        }

        // Nettoyage du projet avec Maven
        stage('Cleaning the project') {
            steps {
                dir(WORKSPACE_DIR) {
                    sh 'mvn clean'
                }
            }
        }

        // Construction de l'artifact
        stage('Artifact construction') {
            steps {
                dir(WORKSPACE_DIR) {
                    sh 'mvn package'
                }
            }
        }

        // Exécution des tests avec Maven
        stage('Tests') {
            steps {
                dir(WORKSPACE_DIR) {
                    sh 'mvn test'
                }
            }
        }

        stage('Code Quality Check via SonarQube') {
            steps {
                script {
                    // SonarQube analysis
                    withSonarQubeEnv('sq') {
                        dir(WORKSPACE_DIR) { // Ajout de dir() pour spécifier le répertoire de travail
                            sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=5ARCTIC6_FOYER_G3 -Dsonar.projectName="5ARCTIC6_FOYER_G3" -Dsonar.host.url=http://20.0.0.21:9000'
                        }
                    }
                }
            }
        }


        // Construction de l'image Docker
        stage('Docker Build') {
            steps {
                script {
                    sh 'docker version'  // Vérification que Docker est disponible
                    dir(WORKSPACE_DIR) {
                        sh 'docker build -t rimbensalah/projetdevops:latest .'
                    }
                }
            }
        }

        // Pousser l'image Docker vers DockerHub
        stage('Pushing Docker Image to DockerHub') {
            steps {
                script {
                    echo 'Pousser l\'image Docker vers DockerHub...'
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                        sh 'docker tag rimbensalah/projetdevops:latest rimbs11/repo_rim:latest'
                        sh 'docker push rimbs11/repo_rim:latest'
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {

                    echo 'Deploying application using Docker Compose...'
                    dir(WORKSPACE_DIR) {
                         sh 'docker-compose up -d'
                    }
                }
            }
        }
    }
}
