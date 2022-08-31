pipeline {
    agent any
    tools {
        jdk 'JDK17'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Publish Unit Test Coverage Report') {
            steps {
                publishCoverage adapters: [jacocoAdapter('build/jacocoReport/test/jacocoTestReport.xml')]
            }
        }

        stage('Generate Docker Image') {
            steps {
                script {
                    docker.withRegistry("https://${NEXUS_HOST}:${NEXUS_PORT}", 'nexusOssCredentials') {
                        def customImage = docker.build("${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/gradle-rest-api-app:${env.BUILD_ID}")
                        customImage.push()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...' 
            }
        }
    }

    post {
        success {
            setBuildStatus("Build succeeded", "SUCCESS");
        }
        failure {
            setBuildStatus("Build failed", "FAILURE");
        }
    }
}

void setBuildStatus(String message, String state) {
    step([
        $class: "GitHubCommitStatusSetter",
        reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/jay-ar-bautista-novare/gradle-rest-api-app"],
        contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
        errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
        statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
    ]);
}