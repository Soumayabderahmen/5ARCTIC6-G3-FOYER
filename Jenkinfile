pipeline {
    agent any

    stages {
        stage('Git Clone') {
            steps {
                script {
                    git branch: 'HedilAouadi-5ARCTIC6-G3', credentialsId: 'Git_devops', url: 'https://github.com/Soumayabderahmen/5ARCTIC6-G3-FOYER.git'
                }
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Publish to Nexus') {
            steps {
                script {
                    echo "Publishing the artifact to Nexus repository..."
                    sh 'mvn deploy -Dmaven.test.skip=true --settings /usr/share/maven/conf/settings.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh 'docker version'
                    sh 'docker build -t hedilinux/projetdevopsimg:latest .'
                    sh 'docker images'
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo \$DOCKER_PASSWORD | docker login --username \$DOCKER_USERNAME --password-stdin"
                    }
                }
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                sh 'docker push hedilinux/projetdevopsimg:latest'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    echo 'Deploying application using Docker Compose...'
                    sh 'cd /var/lib/jenkins/workspace/ProjetDevops && pwd && ls -la && docker-compose -f /var/lib/jenkins/workspace/ProjetDevops/docker-compose.yml up -d'
                }
            }
        }


        stage('Deploy with Monitoring Docker Compose') {
            steps {
                script {
                    echo 'Deploying monitoring services using Docker Compose...'
                    sh 'cd /var/lib/jenkins/workspace/ProjetDevops && pwd && ls -la && docker-compose -f /var/lib/jenkins/workspace/ProjetDevops/docker-compose-monitoring.yml up -d'
                }
            }
        }

        stage('SSH Into k8s Server') {
    steps {
        script {
            def remoteHost = '10.0.0.10'
            def remoteUser = 'vagrant'
            def remotePassword = 'vagrant'

            // Copier les fichiers YAML dans le serveur Kubernetes (k8s master)
            sh """
            sshpass -p ${remotePassword} scp -o StrictHostKeyChecking=no springboot-deployment.yml ${remoteUser}@${remoteHost}:~
            sshpass -p ${remotePassword} scp -o StrictHostKeyChecking=no springboot-service.yml ${remoteUser}@${remoteHost}:~
            sshpass -p ${remotePassword} scp -o StrictHostKeyChecking=no mysql-deployment.yml ${remoteUser}@${remoteHost}:~
            sshpass -p ${remotePassword} scp -o StrictHostKeyChecking=no mysql-pvc.yml ${remoteUser}@${remoteHost}:~
            sshpass -p ${remotePassword} scp -o StrictHostKeyChecking=no mysql-service.yml ${remoteUser}@${remoteHost}:~
            """

            // Appliquer les fichiers de déploiement dans Kubernetes
            sh """
            sshpass -p ${remotePassword} ssh -o StrictHostKeyChecking=no ${remoteUser}@${remoteHost} '
                bash -c "export KUBECONFIG=/etc/kubernetes/admin.conf &&
                kubectl apply -f ~/springboot-deployment.yml &&
                kubectl apply -f ~/springboot-service.yml &&
                kubectl apply -f ~/mysql-deployment.yml &&
                kubectl apply -f ~/mysql-pvc.yml &&
                kubectl apply -f ~/mysql-service.yml"
            '
            """
        }
    }
}

    }
}
