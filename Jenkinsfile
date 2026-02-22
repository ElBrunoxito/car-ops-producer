pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "yobrunox/ms-car-ops-producer"
        DOCKER_TAG   = "${BUILD_NUMBER}"
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
    }
}