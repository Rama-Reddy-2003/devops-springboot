pipeline {
    agent any

    environment {
        KUBECONFIG       = 'C:\\ProgramData\\Jenkins\\.jenkins\\.kube\\config'
        DOCKER_IMAGE     = 'sivajidwarampudi/springboot-employee-app'
        IMAGE_TAG        = "${BUILD_NUMBER}"
        K8S_CONTEXT      = 'docker-desktop'
        DOCKER_HUB_CREDS = credentials('docker-hub-credentials-id') 
    }

    stages {
        stage('Docker Build (Compiles Java inside container)') {
            steps {
                script {
                    echo "Building multi-stage Docker image..."
                    bat "docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Docker Login & Push') {
            steps {
                script {
                    echo "Logging into Docker Hub and pushing image..."
                    bat "docker login -u %DOCKER_HUB_CREDS_USR% -p %DOCKER_HUB_CREDS_PSW%"
                    bat "docker push ${DOCKER_IMAGE}:${IMAGE_TAG}"
                    bat "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo "Deploying to Docker Desktop Kubernetes..."
                    bat "kubectl apply -f k8s.yaml --context=${K8S_CONTEXT}"
                    bat "kubectl set image deployment/springboot-employee-deployment springboot-employee=${DOCKER_IMAGE}:${IMAGE_TAG} --context=${K8S_CONTEXT}"
                    bat "kubectl rollout restart deployment/springboot-employee-deployment --context=${K8S_CONTEXT}"
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo "Checking rollout status..."
                    bat "kubectl rollout status deployment/springboot-employee-deployment --context=${K8S_CONTEXT}"
                    bat "kubectl get pods --context=${K8S_CONTEXT}"
                    bat "kubectl get svc springboot-employee-service --context=${K8S_CONTEXT}"
                }
            }
        }
    }

    post {
        always {
            bat "docker logout"
            echo "POC 4 Pipeline execution completed."
        }
        success {
            echo "Spring Boot application successfully deployed to Kubernetes!"
        }
        failure {
            echo "Pipeline failed. Check logs above."
        }
    }
}