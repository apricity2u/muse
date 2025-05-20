pipeline {
    agent any

    stages {
        stage('Check Docker') {
            steps {
                script {

                    sh 'docker --version'


                    sh 'docker run hello-world'
                }
            }
        }
    }
    post {
        always {
            echo 'Jenkins Pipeline finished.'
        }
    }
}
