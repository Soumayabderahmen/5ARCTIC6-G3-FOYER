pipeline {
  agent any

  environment {
      GITHUB_CREDENTIALS_ID = 'github_soussi_ssh_key'
      DOCKERHUB_CREDENTIALS_ID = 'dockerhub_credentials_soussi'
      EMAIL_RECIPIENT = 'mohamednour.soussi@esprit.tn'
      EMAIL_SUBJECT = 'Jenkins CI/CD Status'
  }
  tools {
      maven 'Maven_HOME'
  }

  stages {
    stage('Install Trivy') {
    steps {
      script {
        echo 'Checking if Trivy is already installed'
        def trivyInstalled = sh(script: 'which trivy', returnStatus: true) == 0
        if (!trivyInstalled) {
          echo 'Trivy not found, installing...'
          sh '''
            wget https://github.com/aquasecurity/trivy/releases/download/v0.45.0/trivy_0.45.0_Linux-64bit.deb
            sudo dpkg -i trivy_0.45.0_Linux-64bit.deb
          '''
        } else {
          echo 'Trivy is already installed'
        }
      }
    }
    }

    stage('Checkout code from remote repository GitHub') {
      steps {
        script{
          echo "Checking out code from the remote repository"
          git url: 'git@github.com:Soumayabderahmen/5ARCTIC6-G3-FOYER.git', branch: 'SoussiMohamedNour-5ARCTIC6-G3', credentialsId: GITHUB_CREDENTIALS_ID
        }
      }
    }

    stage('Cleaning the project') {
      steps {
        echo 'Removing target and .class files ....'
        sh 'mvn clean'
      }
    }
    stage('Compile the project'){
        steps{
            sh 'mvn -Dmaven.test.skip=true package'
        }
    }
    stage('Running unit tests'){
        steps{
            sh 'mvn test'
        }
    }
    stage('Code Quality scan with sonarQube') {
        steps {
            withSonarQubeEnv(installationName: 'Soussi_SonarQube'){
                sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar'
            }
        }
    }
    stage('Quality Gate for SonarQube'){
        steps {
            timeout(time:1,unit:'MINUTES'){
                waitForQualityGate abortPipeline: true
            }
        }
    }
    stage('Upload Artifact to Nexus'){
      steps{
        sh 'mvn deploy -Dmaven.test.skip=true --settings /usr/share/maven/conf/settings.xml'
      }
    }
    stage('Login to DockerHub'){
      steps {
        script {
          echo 'Loggint to DockerHub'
          withCredentials([usernamePassword(credentialsId:DOCKERHUB_CREDENTIALS_ID,usernameVariable:'DOCKERHUB_USERNAME',passwordVariable:'DOCKERHUB_PASSWORD')]){
            sh 'docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD'
          }
        }
      }
    }
    stage('Building Docker image'){
      steps{
        sh 'docker build -t mohamedns/soussimohamednour_g3_foyer:0.1 .'
      }
    }
    stage('Security Scan for docker image with Trivy') {
      steps {
        script {
          echo 'Running security scan with Trivy and saving report'
          sh 'trivy image --severity CRITICAL,HIGH --format json --output trivy-report.json mohamedns/soussimohamednour_g3_foyer:0.1'
        }
      }
    }

    stage('Pushing Backend Image to DockerHub'){
      steps{
        sh 'docker push mohamedns/soussimohamednour_g3_foyer:0.1'
      }
    }
  }
  post {
    always {
      archiveArtifacts artifacts: 'trivy-report.json', allowEmptyArchive: true
    }
  }

}
