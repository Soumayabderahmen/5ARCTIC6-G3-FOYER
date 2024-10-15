pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = 'soumaya_github'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub_credentials' // Ajoutez vos identifiants DockerHub ici

    }

    tools {
        maven 'Maven' // Utilisez la version de Maven configurée
    }

    stages {
        stage('Checkout from Git') {
            steps {
                script {
                    echo "Checking out the repository..."
                    git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER.git', branch: 'AbderahmenSoumaya-5ARCTIC6-G3', credentialsId: "${env.GITHUB_CREDENTIALS_ID}"
                }
            }
        }

        stage('Cleaning the project') {
            steps {
                echo 'Cleaning the project...'
                withMaven(maven: 'Maven') {
                    sh 'mvn clean' 
                }
            }
        }
        stage('Artifact construction') {
            steps {
                script {
                    echo 'Building the project and packaging the artifact...'
                    withMaven(maven: 'Maven') {
                        sh 'mvn package' 
                    }
                }
            }
        }
        stage('Unit Tests') {
            steps {
                echo 'Running unit  tests...'
                withMaven(maven: 'Maven') {
                    sh 'mvn test' 
                }
            }
        }
         /*stage('Code Quality Check via SonarQube') {
                    steps {
                        script {
                            // SonarQube analysis
                            withSonarQubeEnv('SonarQube') {
                                sh './mvnw sonar:sonar'
                            }
                        }
                    }
                }

                stage('Publish to Nexus') {
                    steps {
                        script {
                            // Publish the artifact to Nexus repository
                            sh './mvnw deploy'
                        }
                    }
                }

               */

        stage('Building Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    // Remplacez "your-image-name" par le nom correct de l'image
                    sh 'docker build -t soumayaabderahmen/soumayaabderahmen_g3_foyer .'
                }
            }
        }

        stage('Pushing Docker Image to DockerHub') {
            steps {
                script {
                    echo 'Pushing Docker image to DockerHub...'
                    withCredentials([usernamePassword(credentialsId: "${env.DOCKERHUB_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                        sh 'docker push soumayaabderahmen/soumayaabderahmen_g3_foyer:v1.0.0'
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    echo 'Deploying application using Docker Compose...'
                    sh 'docker-compose up -d'
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