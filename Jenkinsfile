pipeline {
    agent any

    options {
        skipDefaultCheckout(false)
    }

    environment {
        WEBHOOK_URL       = credentials("BUILD_NOTIFICATION_URL")
        IMAGE_NAME        = credentials("IMAGE_NAME")
        SSH_HOST          = credentials("SSH_HOST")
        SSH_PORT          = credentials("SSH_PORT")
        SSH_USER          = credentials("SSH_USER")
        REMOTE_DIR        = '~/work-directory'
        ENV_FILE          = credentials("ENV_FILE")
        DOCKERHUB_CREDENTIAL = credentials("DOCKERHUB_CREDENTIAL")
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage("Load .env") {
            steps {
                withCredentials([file(credentialsId: 'ENV_FILE', variable: 'ENV_FILE_PATH')]) {
                    script {
                        def envMap = readFile(ENV_FILE_PATH).split('\n')
                        def tempEnv = [:]

                        envMap.each { line ->
                            if (line.trim() && !line.startsWith('#')) {
                                def parts = line.tokenize('=')
                                if (parts.size() == 2) {
                                    def key = parts[0].trim()
                                    def value = parts[1].trim()
                                    tempEnv.put(key, value)
                                }
                            }
                        }

                        tempEnv.each { k, v ->
                            env."${k}" = v 
                        }
                    }
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', DOCKERHUB_CREDENTIAL) {
                        def imageTag = "${IMAGE_NAME}:${env.GIT_COMMIT}"
                        def img = docker.build(imageTag)
                        img.push()
                    }
                }
            }
        }

        stage('Deploy to Remote Server') {
            steps {
                sshagent (credentials: ['SSH_CREDENTIAL']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no \
                        -p ${SSH_PORT} ${SSH_USER}@${SSH_HOST} 'mkdir -p ${REMOTE_DIR}'
                        
                        ssh -o StrictHostKeyChecking=no \
                          -p ${SSH_PORT} ${SSH_USER}@${SSH_HOST} << 'EOF'
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
                def repoUrl = env.GIT_URL?.replaceAll(/\.git$/, '') ?: 'Unknown-Repo'
                def linkUrl = env.CHANGE_URL ?: "${repoUrl}/commit/${env.GIT_COMMIT}"
                discordSend(
                    title:      "Build 성공! 🎉",
                    description:"리포지토리: ${repoUrl}\n이벤트 링크: ${linkUrl}",
                    footer:     "Jenkins #${env.BUILD_NUMBER}",
                    link:       env.BUILD_URL,
                    result:     currentBuild.currentResult,
                    webhookURL: env.WEBHOOK_URL
                )
            }
        }
        failure {
            script {
                def repoUrl = env.GIT_URL.replaceAll(/\.git$/, '')
                def linkUrl = env.CHANGE_URL ?: "${repoUrl}/commit/${env.GIT_COMMIT}"
                discordSend(
                    title:      "Build 실패! ❌",
                    description:"리포지토리: ${repoUrl}\n이벤트: ${linkUrl}",
                    footer:     "Jenkins #${env.BUILD_NUMBER}",
                    link:       env.BUILD_URL,
                    result:     currentBuild.currentResult,
                    webhookURL: env.WEBHOOK_URL
                )
            }
        }
    }
}
