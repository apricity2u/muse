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
        stage('원격 배포 준비') {
            steps {
                script {
                    // 상태 초기화
                    env.STATUS_SSH      = '❌'
                    env.STATUS_ENV      = '❌'
                    env.STATUS_BUILD    = '❌'
                    env.STATUS_DEPLOY   = '❌'
                }

                sshagent(credentials: ['SSH_CREDENTIAL']) {
                    script {
                        // 1) SSH 연결 및 코드 클론
                        try {
                            sh """
                                ssh -o StrictHostKeyChecking=no -p ${REMOTE_PORT} \
                                    ${REMOTE_USER}@${REMOTE_HOST} '
                                    sudo rm -rf ${REMOTE_DIR}
                                    mkdir -p ${REMOTE_DIR}
                                    cd ${REMOTE_DIR}
                                    git clone --branch deploy/test ${REPOSITORY_URL} .
                                '
                            """
                            env.STATUS_SSH = '✅'
                        } catch (err) {
                            error("SSH 연결 실패")
                        }

                        // 2) .env 파일 복사
                        try {
                            withCredentials([file(credentialsId: 'ENV_FILE', variable: 'ENV_FILE_PATH')]) {
                                sh """
                                    scp -P ${REMOTE_PORT} -o StrictHostKeyChecking=no \\
                                        \$ENV_FILE_PATH ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/.env
                                """
                            }
                            env.STATUS_ENV = '✅'
                        } catch (err) {
                            error("Env 복사 실패")
                        }

                        // 3) Docker 이미지 빌드
                        try {
                            sh """
                                ssh -o StrictHostKeyChecking=no -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} '
                                    cd ${REMOTE_DIR}
                                    docker buildx build \\
                                        --builder competent_ramanujan \\
                                        --cache-from=type=local,src=/tmp/.buildx-cache/api \\
                                        --cache-to=type=local,dest=/tmp/.buildx-cache/api,mode=max \\
                                        --load \\
                                        -t api-image:latest -f ./api/Dockerfile ./api

                                    docker buildx build \\
                                        --builder competent_ramanujan \\
                                        --cache-from=type=local,src=/tmp/.buildx-cache/client \\
                                        --cache-to=type=local,dest=/tmp/.buildx-cache/client,mode=max \\
                                        --load \\
                                        -t client-image:latest --build-arg API_URL=\${API_URL} \\
                                        -f ./client/Dockerfile ./client
                                '
                            """
                            env.STATUS_BUILD = '✅'
                        } catch (err) {
                            error("빌드 실패")
                        }

                        // 4) Docker 컨테이너 배포
                        try {
                            sh """
                                ssh -o StrictHostKeyChecking=no -p ${REMOTE_PORT} ${REMOTE_USER}@${REMOTE_HOST} '
                                    cd ${REMOTE_DIR}
                                    docker compose up -d --remove-orphans
                                '
                            """
                            env.STATUS_DEPLOY = '✅'
                        } catch (err) {
                            error("배포 실패")
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                // 소요 시간 계산
                def durMillis = currentBuild.duration ?: 0
                def secTotal  = (durMillis / 1000) as int
                def min       = secTotal.intdiv(60)
                def sec       = secTotal % 60
                def timeString = (min > 0) ? "${min}분 ${sec}초" : "${sec}초"

                // 커밋 정보
                def commitId  = (env.GIT_COMMIT ?: sh(script: 'git rev-parse HEAD', returnStdout: true).trim()).take(7)
                def commitMsg = sh(script: 'git log -1 --pretty=%s', returnStdout: true).trim()

                // 브랜치 정보
                def branchName = env.GIT_BRANCH ?: env.BRANCH_NAME

                // 저장소 URL (.git 제거)
                def repoUrl = env.REPOSITORY_URL.replaceAll(/\.git$/, '')

                // Discord 알림
                discordSend(
                    title:      "배포 완료 ✅",
                    description: """
                        **저장소:** ${repoUrl}
                        **브랜치:** ${branchName}
                        **커밋:** `${commitId}` ${commitMsg}
                        **소요 시간:** ${timeString}

                        **단계별 상태:**
                        - ${env.STATUS_SSH}
                        - ${env.STATUS_ENV}
                        - ${env.STATUS_BUILD}
                        - ${env.STATUS_DEPLOY}
                    """.stripIndent().trim(),
                    footer:     "빌드 #${env.BUILD_NUMBER}",
                    result:     currentBuild.currentResult,
                    webhookURL: env.DISCORD_WEBHOOK
                )
            }
        }
        failure {
            script {
                // 소요 시간 계산
                def durMillis = currentBuild.duration ?: 0
                def secTotal  = (durMillis / 1000) as int
                def min       = secTotal.intdiv(60)
                def sec       = secTotal % 60
                def timeString = (min > 0) ? "${min}분 ${sec}초" : "${sec}초"

                // 커밋 정보
                def commitId  = (env.GIT_COMMIT ?: sh(script: 'git rev-parse HEAD', returnStdout: true).trim()).take(7)
                def commitMsg = sh(script: 'git log -1 --pretty=%s', returnStdout: true).trim()

                // 브랜치 정보
                def branchName = env.GIT_BRANCH ?: env.BRANCH_NAME

                // 저장소 URL
                def repoUrl = env.REPOSITORY_URL.replaceAll(/\.git$/, '')

                // Discord 알림
                discordSend(
                    title:      "배포 실패 ❌",
                    description: """
                        **Repository:** ${repoUrl}
                        **Branch:** ${branchName}
                        **Commit:** `${commitId}` ${commitMsg}
                        **Duration:** ${timeString}

                        **단계별 상태:**
                        - ${env.STATUS_SSH} SSH 연결
                        - ${env.STATUS_ENV} Env 복사
                        - ${env.STATUS_BUILD} 빌드
                        - ${env.STATUS_DEPLOY} 배포
                    """.stripIndent().trim(),
                    footer:     "빌드 #${env.BUILD_NUMBER}",
                    result:     currentBuild.currentResult,
                    webhookURL: env.DISCORD_WEBHOOK
                )
            }
        }
    }
}
