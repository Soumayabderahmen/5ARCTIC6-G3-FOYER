pipeline {
    agent any
    
    environment {
        GITHUB_CREDENTIALS_ID='GITHUB_CREDS'
        DOCKERHUB_CREDENTIALS_ID='DOCKER_HUB_CREDS'

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
       stage('Code Quality scan with sonarQube') {
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
        stage('Deploy the spring boot application using docker compose'){
        steps{
        script {
          echo 'Deploying Spring application with Mysql Container using Docker compose'
          sh 'docker-compose -f docker-compose.yaml up -d'
        }
      }
    }
    }
      post {
    always {
       sh 'docker-compose down'     
    }
  }
}

