pipeline {
    agent any
    
    environment {
        GITHUB_CREDENTIALS_ID = 'GITHUB_CREDS'
        DOCKERHUB_CREDENTIALS_ID = 'DOCKER_HUB_CREDS'
    }
    
    tools {
        maven 'MAVEN_HOME'
    }
    
    stages {
        stage('Checkout code from remote repository GitHub') {
            steps {
                script {
                    echo "Checking out code from the remote repository"
                    git url: 'git@github.com:Soumayabderahmen/5ARCTIC6-G3-FOYER.git', branch: 'MootazBenFarhat-5ARCTIC6-G3', credentialsId: GITHUB_CREDENTIALS_ID
                }
            }
        }

        stage('Cleaning the project') {
            steps {
                echo 'Removing target and .class files ....'
                sh 'mvn clean'
            }
        }

        stage('Compile the project') {
            steps {
                sh 'mvn -Dmaven.test.skip=true package'
            }
        }
        
        stage('Code Quality scan with SonarQube') {
            steps {
                withSonarQubeEnv(installationName: 'SONARQUBE_SERVER') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar'
                }
            }
        }
        
        stage('Upload Artifact to Nexus') {
            steps {
                sh 'mvn deploy -Dmaven.test.skip=true --settings /usr/share/maven/conf/settings.xml'
            }
        }
        
        stage('Building Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh 'docker build -t myrtg/mootazbenfarhat-5arctic6-g3:firstpush .'
                }
            }
        }

        stage('Pushing Docker Image to DockerHub') {
            steps {
                script {
                    echo 'Pushing Docker image to DockerHub...'
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                        sh 'docker push myrtg/mootazbenfarhat-5arctic6-g3:firstpush'
                    }
                }
            }
        }
        
        stage('Deploy the Spring Boot application using Docker Compose') {
            steps {
                script {
                    echo 'Deploying Spring application with MySQL Container using Docker Compose'
                    sh 'docker-compose -f docker-compose.yaml up -d'
                }
            }
        }
    }
    
    post {
        always {
            sh 'docker-compose down'
        }
        
        failure {
            script {
                // Optional: Fetch the last lines of the log for the failed stage to include in the email
                def logSnippet = sh(script: "tail -n 50 ${env.WORKSPACE}/logs/${env.STAGE_NAME}.log", returnStdout: true).trim()
                
                // Customize the email content with HTML
                def emailSubject = "❗ Jenkins Pipeline Failed: ${env.JOB_NAME} - Stage: ${env.STAGE_NAME}"
                def emailBody = """
                    <html>
                        <body>
                            <h2 style="color: #D9534F;">Jenkins Pipeline Failure Notification</h2>
                            <p>The Jenkins pipeline <strong>${env.JOB_NAME}</strong> failed at the <strong>${env.STAGE_NAME}</strong> stage.</p>
                            
                            <h3>Job Details</h3>
                            <ul>
                                <li><strong>Job Name:</strong> ${env.JOB_NAME}</li>
                                <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                                <li><strong>Failed Stage:</strong> ${env.STAGE_NAME}</li>
                                <li><strong>Console Output:</strong> <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></li>
                            </ul>

                            <h3>Log Snippet</h3>
                            <pre style="background-color: #F8F9FA; padding: 10px; border: 1px solid #E1E1E8; border-radius: 5px; color: #333;">${logSnippet}</pre>
                            
                            <p>Please review the logs and take necessary actions.</p>

                            <p>Regards,<br>Jenkins</p>
                        </body>
                    </html>
                """

                // Send the email notification
                mail to: 'mootaz.benfarhat@esprit.tn',
                     subject: emailSubject,
                     body: emailBody,
                     mimeType: 'text/html'
            }
        }
    }
}

