pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = 'soumaya_github'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub_credentials_soumaya' // Ajoutez vos identifiants DockerHub ici
        EMAIL_RECIPIENT = 'soumayaabderahmen44@gmail.com'
        EMAIL_SUBJECT = 'Statut du Build Jenkins'
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
                        sh 'mvn -Dmaven.test.skip=true package'
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
         stage('Start SonarQube Container') {
                    steps {
                        script {
                            echo 'Starting SonarQube container...'
                            sh 'docker start sonarqube'
                        }
                    }
                }
         stage('Code Quality Check via SonarQube') {
    steps {
        script {
            // SonarQube analysis
            withSonarQubeEnv('SonarQube') {
                sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=5ARCTIC6_FOYER_G3 -Dsonar.projectName="5ARCTIC6_FOYER_G3" -Dsonar.host.url=http://192.168.56.10:9000'
            }
        }
    }
}

                stage('Publish to Nexus') {
                    steps {
                        script {
                            // Publish the artifact to Nexus repository
                            sh 'mvn deploy'
                        }
                    }
                }



        stage('Building Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    // Remplacez "your-image-name" par le nom correct de l'image
                    sh 'docker build -t soumayaabderahmen/soumayaabderahmen_g3_foyer:v1.0.0 .'
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
           always {
               script {
                   def jobName = env.JOB_NAME
                   def buildNumber = env.BUILD_NUMBER
                   def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
                   def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'

                   def body = """<html>
                   <body>
                       <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                           <h2>${jobName} - Build ${buildNumber}</h2>
                           <div style="background-color: ${bannerColor}; padding: 10px;">
                               <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
                           </div>
                           <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
                       </div>
                   </body>
                   </html>"""

                   emailext (
                       subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                       body: body,
                       to: 'soumayaabderahmen44@gmail.com',
                       from: 'jenkins@example.com',
                       replyTo: 'jenkins@example.com',
                       mimeType: 'text/html',

                   )
               }
           }
       }
   }