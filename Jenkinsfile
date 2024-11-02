piepline {
  agent any

  environment {
      GITHUB_CREDENTIALS_ID = 'github_soussi_token'
      DOCKERHUB_CREDENTIALS_ID = 'dockerhub_credentials_soussi'
      EMAIL_RECIPIENT = 'mohamednour.soussi@esprit.tn'
      EMAIL_SUBJECT = 'Jenkins CI/CD Status'
      // NEXUS_CREDENTIALS_ID = 'nexus_credentials_id'
  }
  tools {
      maven 'Maven'
  }

  stages {
    stage('Checkout code from remote repository GitHub') {
      steps {
        script{
          echo "Checking out code from the remote repository"
          git url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER.git', branch: 'SoussiMohamedNour-5ARCTIC6-G3', credentialsId: GITHUB_CREDENTIALS_ID
        }
      }
    }

    stage('Cleaning the project') {
      steps {
        echo 'Removing target and .class files ....'
        withMaven(maven: 'Maven'){
          sh 'mvn clean'
        }
      }

    }
  }
}
