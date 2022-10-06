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
//        stage('Publish Unit Test Coverage Report') {
//            steps {
//                publishCoverage adapters: [jacocoAdapter('build/jacocoReport/test/jacocoTestReport.xml')]
//            }
//        }

//        stage('Generate Docker Image') {
//            steps {
//                script {
//                    docker.withRegistry("https://${NEXUS_HOST}:${NEXUS_PORT}", 'nexusOssCredentials') {
//                        def customImage = docker.build("${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/gradle-rest-api-app:${env.GIT_COMMIT}")
//                        customImage.push()
//                    }
//                }
//            }
//        }

        stage('Deploy') {
                   steps {
                      //'oc image mirror mysourceregistry.com/myimage:latest mydestinationegistry.com/myimage:latest'
                   load "https://raw.githubusercontent.com/jay-ar-bautista-novare/gradle-rest-api-app/feature/Deploy/oc_templates/env.settings"
                      
                    script {
                        
                        openshift.withCluster( 'openshift cluster' ) {
          
                            openshift.withProject( 'apcdevpoc-project-dev' ) {
                                //def app = openshift.newApp("'rodexter6/rest-api-sample'","'--as-deployment-config' '--dry-run'")
                                //--IMAGE STREAM-------------------------------------------------------------------------------------------------------------------------------------------------------                                
                                def ispatch = [
                                      "kind": "ImageStream",
                                      "apiVersion": "image.openshift.io/v1",
                                      "metadata": [
                                        "name": 'rest-api-sample-develop',
                                        "namespace": "apcdevpoc-project-dev"
                                      ],
                                      "spec": [
                                        "lookupPolicy": [
                                          "local": false
                                        ],
                                        "tags": [
                                          [
                                            "name": 'latest',
                                            "from": [
                                              "kind": "DockerImage",
                                              "name": "${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/"+'rest-api-sample'+':'+'latest'
                                            ],
                                            "generation": 2,
                                            "importPolicy": [
                                              "insecure": true
                                            ],
                                            "referencePolicy": [
                                              "type": "Local"
                                            ]
                                          ]
                                        ]
										
                                      ]
                                    ]
                                openshift.apply(ispatch)
                                
                                echo ('create/update deployment config')
                                openshift.raw("apply --filename=https://raw.githubusercontent.com/jay-ar-bautista-novare/gradle-rest-api-app/feature/Deploy/oc_templates/deploymentConfig.yaml")
                                
                                echo ('create/update service')
                                openshift.raw("apply --filename=https://raw.githubusercontent.com/jay-ar-bautista-novare/gradle-rest-api-app/feature/Deploy/oc_templates/service.yaml")
                                
                                echo ('create/update route')
                                openshift.raw("apply --filename=https://raw.githubusercontent.com/jay-ar-bautista-novare/gradle-rest-api-app/feature/Deploy/oc_templates/route.yaml")

                            }
                        }
                    }
                   }
        }

//        stage('Functional Test') {
//           steps {
//                sh 'pip3 install robotframework'
//                sh 'pip3 install robotframework-jsonlibrary'
//                sh 'pip3 install robotframework-extendedrequestslibrary'
//                sh 'python3 -m robot src/test/robot/RequestAPI.robot'
//                step([$class: 'RobotPublisher', disableArchiveOutput: false, logFileName: 'log.html', otherFiles: '', outputFileName: 'output.xml', outputPath: '', passThreshold: 100, reportFileName: 'report.html', unstableThreshold: 0])
//            }
//        }
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
