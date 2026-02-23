pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "yobrunox/ms-car-ops-producer"
        DOCKER_TAG   = "${BUILD_NUMBER}"
        DOCKER_CREDS = credentials('dockerhub-creds')

    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/ElBrunoxito/car-ops-producer.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    docker login -u $DOCKER_CREDS_USR -p $DOCKER_CREDS_PSW
                    docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                '''
            }
        }
        stage('Deploy to AKS (FIRST TIME)') {
            steps {
                sh """
                kubectl apply -f k8k/kafka-deployment.yaml
                kubectl apply -f k8k/kafka-service.yaml

                kubectl apply -f k8k/configmap.yaml
                kubectl apply -f k8k/deployment.yaml
                kubectl apply -f k8k/service.yaml
                kubectl apply -f k8k/ingress.yaml
                """
            }
        }
        
    }
}