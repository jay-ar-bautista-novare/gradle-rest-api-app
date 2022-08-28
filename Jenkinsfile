pipeline {
    agent any
    triggers {
        githubPush()
    }

    stages {
        stage('Build') {
            steps {     
                echo 'Building...'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
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
    
    void setBuildStatus(String message, String state) {
        step([
          $class: "GitHubCommitStatusSetter",
          reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/my-org/my-repo"],
          contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
          errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
          statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
      ]);
}
}
