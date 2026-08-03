pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'sivajidwarampudi/springboot-employee-app:latest'
    }

    stages {
        stage('Compile & Package') {
            steps {
                // Uses the Windows Maven wrapper script pulled from SCM
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                    bat "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat 'kubectl apply -f k8s.yaml'
                bat 'kubectl rollout restart deployment/springboot-employee-deployment'
            }
        }
    }
}