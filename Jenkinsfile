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
        NEXUS_CREDENTIALS_ID = 'Nexus' // Identifiants pour Nexus
        NEXUS_URL = '20.0.0.21:8081' // URL de Nexus
        NEXUS_REPOSITORY = 'devopsproject' // Nom du repository dans Nexus
        ARTIFACT_ID = 'testartifact' // ID de l'artefact
        GROUP_ID = 'org.springframework.boot' // Groupe ID
        VERSION = '1.0.0-SNAPSHOT' // Version de l'artefact
        FILE = "${WORKSPACE_DIR}/target/Foyer-0.0.1-SNAPSHOT.jar" // Chemin complet vers l'artefact
        ARTIFACT_TYPE = 'jar' // Type d'artefact
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
                                git config --global http.postBuffer 1610612736

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
                    echo 'Starting SonarQube container if not already running...'
                    // Démarre SonarQube s'il n'est pas en cours d'exécution
                    def sonarStatus = sh(script: "docker ps --filter 'name=sonarqube' --filter 'status=running' --format '{{.Names}}'", returnStdout: true).trim()
                    if (sonarStatus != 'sonarqube') {
                        sh 'docker start sonarqube'
                        echo 'SonarQube container started.'
                    } else {
                        echo 'SonarQube is already running.'
                    }

                    // Exécution de l'analyse SonarQube
                    withSonarQubeEnv('sq') {
                        dir(WORKSPACE_DIR) { // Spécifie le répertoire de travail
                            sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=5ARCTIC6_FOYER_G3 -Dsonar.projectName="5ARCTIC6_FOYER_G3" -Dsonar.host.url=http://20.0.0.21:9000'
                        }
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    echo 'Starting Nexus container if not already running...'
                    // Vérifie si le conteneur Nexus est en cours d'exécution
                    def nexusStatus = sh(script: "docker ps --filter 'id=12f184d7b91e' --filter 'status=running' --format '{{.ID}}'", returnStdout: true).trim()
                    if (nexusStatus != '12f184d7b91e') {
                        sh 'docker start 12f184d7b91e'
                        echo 'Nexus container started.'
                    } else {
                        echo 'Nexus is already running.'
                    }

                    echo 'Déploiement de l\'artefact vers Nexus...'
                    dir(WORKSPACE_DIR) {
                        nexusArtifactUploader artifacts: [[artifactId: ARTIFACT_ID, classifier: '', file: FILE, type: ARTIFACT_TYPE]],
                            credentialsId: NEXUS_CREDENTIALS_ID,
                            groupId: GROUP_ID,
                            nexusUrl: NEXUS_URL,
                            nexusVersion: 'nexus3',
                            protocol: 'http',
                            repository: NEXUS_REPOSITORY,
                            version: VERSION
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
                        sh 'docker-compose down -v'
                        // Redéploie les conteneurs
                        sh 'docker-compose up -d'                    }
                }
            }
        }

        stage('Monitoring using prometheus and grafana ') {
            steps {
                script {
                    echo 'Removing existing containers and volumes, then redeploying with Docker Compose...'
                    dir(WORKSPACE_DIR) {
                        // Supprime les conteneurs et les volumes définis dans docker-compose-monitor.yml
                        sh 'docker-compose -f docker-compose-monitor.yml down '
                        // Redéploie les conteneurs
                        sh 'docker-compose -f docker-compose-monitor.yml up -d'
                    }
                }
            }
        }
    }
}
