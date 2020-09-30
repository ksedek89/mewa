pipeline {
    agent any
    stages {
        stage('init env var') {
            steps {
                script{
                    env.NPM_VERSION = sh(returnStdout: true, script: "./version.sh").trim()
                    env.NPM_NEXT_VERSION = sh(returnStdout: true, script: "./version.sh --patch --just-print").trim()
                    env.DOCKER_SECRET = 'artifactory';
                    env.DEPLOY_BUILD_DATE = sh(returnStdout: true, script: "date +'%Y-%m-%d %H:%M'").trim()
                    env.LAST_COMMIT_AUTHOR = sh(returnStdout: true, script: 'git log -1 --format=format:"%aN"').trim()
                    env.BRANCH_TYPE = sh(returnStdout: true, script: "echo ${BRANCH_NAME} | cut -d / -f 1").trim()
                    env.GIT_URL_FOR_JENKINS = 'lwgit-01.production.local:7990/scm/yel/portal-be-starter.git'
                    env.JENKINS_GIT_CREDENTIALS_ID = 'lwgit-01'

//                  PROJECT SPECIFIC:
                    jdk = tool name: 'openjdk11'
                    env.JAVA_HOME="${jdk}"
                    env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
                    sh 'java -version'
                }
            }
        }

        stage('CI: build') {
            when {
                not {
                    anyOf {
                        tag "*"; branch 'master'; branch 'develop'; branch 'release/*'; branch 'hotfix_release/*'
                    }
                }
            }
            steps{
                withCredentials([usernamePassword(credentialsId: "${DOCKER_SECRET}", usernameVariable: 'login', passwordVariable: 'password')]) {
                    sh "./gradlew clean build -PdockerNexusUrl='${ARTIFACTORY_URL}' -PpruMavenPassword='${password}' -PpruMavenUser='${login}'"
                }
            }
        }

        // CD
        stage('CD: next patch version') {
            when {
                allOf {
                    not {
                        environment name: 'LAST_COMMIT_AUTHOR', value: "${JENKINS_GIT_FULL_NAME}"
                    }
                    anyOf {
                        branch 'develop'; branch 'release/*'; branch 'hotfix_release/*'
                    }
                }
            }
            steps {
                sh 'printenv'
                sh 'git tag -d v${NPM_NEXT_VERSION} || echo "No broken tags to remove"'
                git url: "http://jenkins_dev@${GIT_URL_FOR_JENKINS}",
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
        }
        stage('CD: build image and push to nexus') {
            when {
                anyOf {
                    branch 'master';
                    allOf {
                        environment name: 'LAST_COMMIT_AUTHOR', value: "${JENKINS_GIT_FULL_NAME}";
                        anyOf {
                            branch 'develop'; branch 'release/*'; branch 'hotfix_release/*'
                        }
                    }
                }
            }
            steps {
              	withCredentials([usernamePassword(credentialsId: "${DOCKER_SECRET}", usernameVariable: 'login', passwordVariable: 'password')]) {
                  sh "./gradlew dockerBuildAndPushImage -PimageVersion=${BRANCH_TYPE}-${NPM_VERSION} -PdockerNexusUrl='${ARTIFACTORY_URL}' -PpruMavenPassword='${password}' -PpruMavenUser='${login}'"
                  sh "./gradlew dockerBuildAndPushImage -PimageVersion=${BRANCH_TYPE}-latest -PdockerNexusUrl='${ARTIFACTORY_URL}' -PpruMavenPassword='${password}' -PpruMavenUser='${login}'"
                }
            }
        }
        stage('CD: install on DEV') {
            when {
                allOf {
                    environment name: 'LAST_COMMIT_AUTHOR', value: "${JENKINS_GIT_FULL_NAME}";
                    branch 'develop';
                }
            }
            steps {
//                 sh 'ssh -i ~/.ssh/ksed_key  ${SSH_LOGIN}@${DEV_HOST} ./installPsaFe.sh dev ${BRANCH_TYPE}-${NPM_VERSION}'
                sh 'echo step skipped'
            }
        }
        stage('CD: install on DEV2') {
            when {
                allOf {
                    environment name: 'LAST_COMMIT_AUTHOR', value: "${JENKINS_GIT_FULL_NAME}";
                    branch 'develop';
                }
            }
            steps {
//                 sh 'ssh -i ~/.ssh/ksed_key  ${SSH_LOGIN}@${DEV2_HOST} ./installPsaFe.sh dev2 ${BRANCH_TYPE}-${NPM_VERSION}'
                sh 'echo step skipped'
            }
        }

        // TAG
        stage('TAG: build image and push to nexus') {
            when {
                tag "*";
            }
            steps {
              	withCredentials([usernamePassword(credentialsId: "${DOCKER_SECRET}", usernameVariable: 'login', passwordVariable: 'password')]) {
                	sh "./gradlew dockerBuildAndPushImage -PimageVersion=${BRANCH_TYPE} -PdockerNexusUrl='${ARTIFACTORY_URL}' -PpruMavenPassword='${password}' -PpruMavenUser='${login}'"
                }
            }
        }
        stage('TAG: install on SWAP') {
            when {
                tag "*";
            }
            steps {
//                 sh 'ssh -i ~/.ssh/ksed_key  ${SSH_LOGIN}@${SWAP_HOST} ./installPsaFe.sh swap ${BRANCH_TYPE}'
                sh 'echo step skipped'
            }
        }
    }
    post {
        always {
            cleanWs()
            script{
                if (env.BRANCH_NAME == 'master' ||
                    (env.LAST_COMMIT_AUTHOR == "${JENKINS_GIT_FULL_NAME}" &&
                        (env.BRANCH_NAME == 'develop' ||
                        env.BRANCH_NAME =~ 'release/*' ||
                        env.BRANCH_NAME =~ 'hotfix_release/*')
                    )
                ) {
                    notifyBuild(currentBuild.currentResult)
                } else {
                    sh 'echo step skipped'
                }
            }
        }
    }
}

void notifyBuild(String buildStatus = 'STARTED') {
    buildStatus =  buildStatus ?: 'UNKNOWN'

    def color = 'BLUE'
    def colorCode = '#0000FF'

    if (buildStatus == 'STARTED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
    } else if (buildStatus == 'SUCCESS') {
        color = 'GREEN'
        colorCode = '#00FF00'
    } else {
        color = 'RED'
        colorCode = '#FF0000'
    }
    def jobName = env.JOB_NAME.split('/')[0]
    def projectUrl = "${JENKINS_URL}job/${jobName}"

    def message = """*${jobName}*   v._${NPM_VERSION}_   ${buildStatus}!
:building_construction:<${projectUrl}|${jobName}>   :herb:<${JOB_URL}|${env.BRANCH_NAME}>   :construction:<${BUILD_URL}|${env.BUILD_NUMBER}>   :spiral_note_pad:<${BUILD_URL}console|console>"""

    slackSend (color: colorCode, message: message)
}
