pipeline {
    agent any

    environment {
        WEBHOOK_URL = credentials("Total-Back-Office-BE-Discord-Webhook")
    }

    stages {
        stage('Start') {
            steps {
                echo "빌드 시작 🚀"
            }
        }
    }

    post {
        success {
            script {

                def repoUrl   = env.GIT_URL.replaceAll(/\.git$/, '')
                def linkUrl   = env.CHANGE_URL ?: "${repoUrl}/commit/${env.GIT_COMMIT}"
                
                discordSend(
                  title:       "Build 성공! 🎉",
                  description: "리포지토리: ${repoUrl}\n이벤트 링크: ${linkUrl}",
                  footer:      "Jenkins #${env.BUILD_NUMBER}",
                  link:        env.BUILD_URL,
                  result:      currentBuild.currentResult,
                  webhookURL:  env.WEBHOOK_URL
                )
            }
        }

        failure {
            script {
                def repoUrl = env.GIT_URL.replaceAll(/\.git$/, '')
                def linkUrl = env.CHANGE_URL ?: "${repoUrl}/commit/${env.GIT_COMMIT}"
                discordSend(
                  title:       "Build 실패! ❌",
                  description: "리포지토리: ${repoUrl}\n이벤트 링크: ${linkUrl}",
                  footer:      "Jenkins #${env.BUILD_NUMBER}",
                  link:        env.BUILD_URL,
                  result:      currentBuild.currentResult,
                  webhookURL:  env.WEBHOOK_URL
                )
            }
        }
    }
}
