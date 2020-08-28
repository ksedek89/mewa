node {
    try {
        jdk = tool name: 'openjdk11'
        env.JAVA_HOME="${jdk}"
        env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
        sh 'java -version'

        stage('Checkout' ) {
            checkout scm
        }

        stage('Init env variables') {
            env.NPM_VERSION = sh(returnStdout: true, script: "./version.sh").trim()
            env.NPM_NEXT_VERSION = sh(returnStdout: true, script: "./version.sh --patch --just-print").trim()
            env.LAST_COMMIT_AUTHOR = sh(returnStdout: true, script: 'git log -1 --format=format:"%aN"').trim()
            env.BRANCH_TYPE = sh(returnStdout: true, script: "echo ${BRANCH_NAME} | cut -d / -f 1").trim()
            env.GIT_URL_FOR_JENKINS = 'bitbucket.dev.prulocal/scm/ap/portal-be-starter.git'
            env.JENKINS_GIT_CREDENTIALS_ID = '27058a19-9e93-40da-bff4-181bf92bc29c'

            if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.startsWith('release/') || env.BRANCH_NAME.startsWith('hotfix_release/')) {
                env.IS_CD_BRANCH = 'true'
            }
            sh 'printenv'
        }
        if(env.BRANCH_NAME == 'master' || env.IS_CD_BRANCH)
        {
            // ====++++ CD pipeline ++++====
            if((env.LAST_COMMIT_AUTHOR != 'jenkins' && env.IS_CD_BRANCH)) {
                stage('CD: next patch version') {
                    sh 'git tag -d v${NPM_NEXT_VERSION} || echo "No broken tags to remove"'
                    git url: "http://jenkins@${GIT_URL_FOR_JENKINS}",
                        credentialsId: "${JENKINS_GIT_CREDENTIALS_ID}",
                        branch: "${BRANCH_NAME}"
                    sh 'printenv'
                    sh 'git remote -v'
                    sh 'git reset origin/${BRANCH_NAME} --hard'
                    sh 'git branch -u origin/${BRANCH_NAME}'
                    sh './version.sh --patch'
                    sh 'git commit -m "v${NPM_NEXT_VERSION}" version'
                    sh 'git tag -a "v${NPM_NEXT_VERSION}" -m "v${NPM_NEXT_VERSION}"'
                    sh 'git log -1 --format=format:"%aN"'
                    withCredentials([usernamePassword(credentialsId: "${JENKINS_GIT_CREDENTIALS_ID}", usernameVariable: 'username', passwordVariable: 'password')]) {
                        sh("git push --follow-tags http://${username}:${password}@${GIT_URL_FOR_JENKINS}")
                    }
                }
            } else {
                stage('CD: Gradle build docker image') {
                    sh 'sh gradlew clean build dockerBuildImage'
                }
                stage('CD: Gradle push docker image') {
                    sh 'sh gradlew dockerPushImage -PimageTag=${BRANCH_TYPE}-${NPM_VERSION}'
                    sh 'sh gradlew dockerPushImage -PimageTag=${BRANCH_TYPE}-latest'
                }
            }
        } else {
            // ====++++ CI pipeline ++++====
            stage('CI: Gradle build') {
                sh 'sh gradlew clean build'
            }
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        throw e
    } finally {
        stage('clean workspace') {
            cleanWs()
        }
        if (env.BRANCH_NAME == 'master') {
            notifyBuild(currentBuild.result)
        }
    }
}

def notifyBuild(String buildStatus = 'STARTED') {
    buildStatus =  buildStatus ?: 'SUCCESSFUL'

    def colorName = 'RED'
    def colorCode = '#FF0000'
    def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"

    if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
    } else if (buildStatus == 'SUCCESSFUL') {
        color = 'GREEN'
        colorCode = '#00FF00'
    } else {
        color = 'RED'
        colorCode = '#FF0000'
    }

    slackSend (color: colorCode, message: summary)
}
