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
        stage('브랜치 정보 설정') {
            steps {
                script {
                    def rawBranch   = env.GIT_BRANCH ?: ''
                    def branchShort = rawBranch.replaceFirst(/^origin\//, '')
                    env.BRANCH_NAME = branchShort ?: 'main'

                    env.STATUS_SSH    = '❌'
                    env.STATUS_ENV    = '❌'
                    env.STATUS_BUILD  = '❌'
                    env.STATUS_DEPLOY = '❌'
                    env.STATUS_HEALTH = '❌'
                }
            }
        }

        stage('원격 배포 준비') {
            steps {
                sshagent(credentials: ['SSH_CREDENTIAL']) {
                    script {
                        // 1) SSH 연결 및 코드 클론
                        try {
                            sh """
                                ssh -o StrictHostKeyChecking=no -p ${REMOTE_PORT} \
                                    ${REMOTE_USER}@${REMOTE_HOST} '
                                    sudo rm -rf ${REMOTE_DIR}
                                    mkdir -p ${REMOTE_DIR}
                                    mkdir -p /tmp/.buildx-cache/api /tmp/.buildx-cache/client
                                    cd ${REMOTE_DIR}
                                    git clone --branch ${env.BRANCH_NAME} ${REPOSITORY_URL} .
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
                // 소요 시간
                def dur   = currentBuild.duration ?: 0
                def secs  = (dur / 1000) as int
                def m     = secs.intdiv(60)
                def s     = secs % 60
                def time  = (m>0) ? "${m}분 ${s}초" : "${s}초"

                // 커밋/메타 정보
                def commitId  = (env.GIT_COMMIT ?: sh(script:'git rev-parse HEAD',returnStdout:true).trim()).take(7)
                def commitMsg = sh(script:'git log -1 --pretty=%s',returnStdout:true).trim()
                def author    = sh(script:'git log -1 --pretty=format:%an',returnStdout:true).trim()
                def causes = currentBuild.getBuildCauses() 
                def trigger = (causes && causes.size()>0) ? causes[0].shortDescription : 'Unknown'
                def repo      = env.REPOSITORY_URL.replaceAll(/\.git$/,'')
                def jobUrl    = env.BUILD_URL ?: ''
                def branch    = env.BRANCH_NAME

                discordSend(
                    title:    "배포 완료 ✅",
                    description: """
                        **Repository:** ${repo}
                        **Branch:** ${branch}
                        **Commit:** `${commitId}` ${commitMsg}
                        **Author:** ${author}
                        **Triggered By:** ${trigger}
                        **Duration:** ${time}
                        **Job:** [#${env.BUILD_NUMBER}](${jobUrl})

                        **STEPS:**
                        - ${env.STATUS_SSH} SSH 연결
                        - ${env.STATUS_ENV} Env 복사
                        - ${env.STATUS_BUILD} 빌드
                        - ${env.STATUS_DEPLOY} 배포
                    """.stripIndent().trim(),
                    footer:     "빌드 #${env.BUILD_NUMBER}",
                    result:     currentBuild.currentResult,
                    link: jobUrl,
                    webhookURL: env.DISCORD_WEBHOOK
                )
            }
        }
        failure {
            script {
                def dur   = currentBuild.duration ?: 0
                def secs  = (dur / 1000) as int
                def m     = secs.intdiv(60)
                def s     = secs % 60
                def time  = (m>0) ? "${m}분 ${s}초" : "${s}초"

                def commitId  = (env.GIT_COMMIT ?: sh(script:'git rev-parse HEAD',returnStdout:true).trim()).take(7)
                def commitMsg = sh(script:'git log -1 --pretty=%s',returnStdout:true).trim()
                def author    = sh(script:'git log -1 --pretty=format:%an',returnStdout:true).trim()
                def causes = currentBuild.rawBuild.getCauses()
                def trigger = (causes && causes.size()>0) ? causes[0].shortDescription : 'Unknown'
                def repo      = env.REPOSITORY_URL.replaceAll(/\.git$/,'')
                def jobUrl    = env.BUILD_URL ?: ''
                def branch    = env.BRANCH_NAME

                discordSend(
                    title:    "배포 실패 ❌",
                    description: """
                        **Repository:** ${repo}
                        **Branch:** ${branch}
                        **Commit:** `${commitId}` ${commitMsg}
                        **Author:** ${author}
                        **Triggered By:** ${trigger}
                        **Duration:** ${time}
                        **Job:** [#${env.BUILD_NUMBER}](${jobUrl})

                        **STEPS:**
                        - ${env.STATUS_SSH} SSH 연결
                        - ${env.STATUS_ENV} Env 복사
                        - ${env.STATUS_BUILD} 빌드
                        - ${env.STATUS_DEPLOY} 배포
                    """.stripIndent().trim(),
                    footer:     "빌드 #${env.BUILD_NUMBER}",
                    result:     currentBuild.currentResult,
                    link: jobUrl,
                    webhookURL: env.DISCORD_WEBHOOKgit
                )
            }
        }
    }
}
