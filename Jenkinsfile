pipeline {
agent any

```
options {
    skipDefaultCheckout(false)
}

environment {
    WEBHOOK_URL          = credentials("BUILD_NOTIFICATION_URL")
    IMAGE_NAME           = credentials("IMAGE_NAME")
    SSH_HOST             = credentials("SSH_HOST")
    SSH_PORT             = credentials("SSH_PORT")
    SSH_USER             = credentials("SSH_USER")
    REMOTE_DIR           = '~/work-directory'
    DOCKERHUB_CREDENTIAL = credentials("DOCKERHUB_CREDENTIAL")
}

stages {
    stage('Checkout') {
        steps {
            checkout scm
        }
    }

    stage('Load .env') {
        steps {
            withCredentials([file(credentialsId: 'ENV_FILE', variable: 'ENV_FILE_PATH')]) {
                sh 'cp ${ENV_FILE_PATH} .env'
                sh 'chmod 644 .env'
            }
        }
    }

    stage('Build & Push Docker Image') {
        steps {
            script {
                docker.withRegistry('', DOCKERHUB_CREDENTIAL) {
                    def imageTag = "${IMAGE_NAME}:${env.GIT_COMMIT}"
                    docker.build(imageTag).push()
                }
            }
        }
    }

    stage('Deploy to Remote Server') {
        steps {
            sshagent(credentials: ['SSH_CREDENTIAL']) {
                sh """
                    ssh -o StrictHostKeyChecking=no -p ${SSH_PORT} ${SSH_USER}@${SSH_HOST} 'mkdir -p ${REMOTE_DIR}'
                    ssh -o StrictHostKeyChecking=no -p ${SSH_PORT} ${SSH_USER}@${SSH_HOST} << 'EOF'
                      cd ${REMOTE_DIR}
                      docker pull ${IMAGE_NAME}:${env.GIT_COMMIT}
                      docker compose down
                      docker compose up -d
                      docker system prune -f
                    EOF
                """
            }
        }
    }
}

post {
    success {
        script {
            String gitUrl = env.GIT_URL ?: ''
            String repoUrl = gitUrl ? gitUrl.replaceAll(/\.git\$/, '') : 'https://github.com/unknown/repo'
            String linkUrl = env.CHANGE_URL ?: "${repoUrl}/commit/${env.GIT_COMMIT}"

            discordSend(
                title: 'Build 성공! 🎉',
                description: "리포지토리: ${repoUrl}\n이벤트 링크: ${linkUrl}",
                footer: "Jenkins #${env.BUILD_NUMBER}",
                link: env.BUILD_URL ?: '',
                result: currentBuild.currentResult,
                webhookURL: WEBHOOK_URL
            )
        }
    }

    failure {
        script {
            String gitUrl = env.GIT_URL ?: ''
            String repoUrl = gitUrl ? gitUrl.replaceAll(/\.git\$/, '') : 'https://github.com/unknown/repo'
            String linkUrl = env.CHANGE_URL ?: "${repoUrl}/commit/${env.GIT_COMMIT}"

            discordSend(
                title: 'Build 실패! ❌',
                description: "리포지토리: ${repoUrl}\n이벤트 링크: ${linkUrl}",
                footer: "Jenkins #${env.BUILD_NUMBER}",
                link: env.BUILD_URL ?: '',
                result: currentBuild.currentResult,
                webhookURL: WEBHOOK_URL
            )
        }
    }
}
```

}
