pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "news-aggregator"
        DOCKER_TAG = "latest"
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/exemplaire18/NewsAggregatorApplication.git'
            }
        }
        stage('Build Backend') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Build Frontend') {
            steps {
                dir('news-aggregator-ui') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        stage('Deploy Locally') {
            steps {
                sh 'docker-compose -f docker/docker-compose.yml up -d'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}