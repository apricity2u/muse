pipeline {
    agent any

    environment {
        REMOTE_HOST     = credentials('SSH_HOST')
        REMOTE_PORT     = credentials('SSH_PORT')
        REMOTE_USER     = credentials('SSH_USER')
        DISCORD_WEBHOOK = credentials('BUILD_NOTIFICATION_URL')
        REMOTE_DIR      = '~/work-directory'
        REPOSITORY_URL  = 'https://github.com/apricity2u/muse.git'
    }

    stages {
        stage('원격 배포') {
            steps {
                sshagent(credentials: ['SSH_CREDENTIAL']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} '
                        sudo rm -rf ${REMOTE_DIR}
                        mkdir -p ${REMOTE_DIR}
                        cd ${REMOTE_DIR}

                        if [ ! -d .git ]; then
                          git clone --branch ${env.BRANCH_NAME} ${REPOSITORY_URL} .
                        else
                          git fetch origin
                          git reset --hard origin/${env.BRANCH_NAME}
                        fi
                        '
                    """

                    withCredentials([file(credentialsId: 'ENV_FILE', variable: 'ENV_FILE_PATH')]) {
                        sh """
                            scp -P ${REMOTE_PORT} -o StrictHostKeyChecking=no \$ENV_FILE_PATH ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/.env
                        """
                    }

                    sh """
                        ssh -o StrictHostKeyChecking=no -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} '
                            cd ${REMOTE_DIR}

                            docker compose up -d --remove-orphans
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            discordSend(
                title: '✅ 배포 성공',
                description: 'Jenkins에서 서비스 배포가 성공적으로 완료되었습니다.',
                link: env.BUILD_URL,
                webhookURL: "${DISCORD_WEBHOOK}",
                result: currentBuild.currentResult
            )
        }
        failure {
            discordSend(
                title: '❌ 배포 실패',
                description: 'Jenkins에서 서비스 배포 중 오류가 발생했습니다.',
                link: env.BUILD_URL,
                webhookURL: "${DISCORD_WEBHOOK}",
                result: currentBuild.currentResult
            )
        }
    }
}