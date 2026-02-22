pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/ElBrunoxito/car-ops-producer'
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean package'
      }
    }
  }
}