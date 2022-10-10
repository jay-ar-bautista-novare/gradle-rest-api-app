pipeline {
    agent any
    tools {
        jdk 'JDK17'
        oc 'oc'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
                
                //Generate Docker Image
                script {
	                docker.withRegistry("https://${NEXUS_HOST}:${NEXUS_PORT}", 'nexusOssCredentials') {
	                	def customImage = docker.build("${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/gradle-rest-api-app:${env.GIT_COMMIT}")
	                    customImage.push()
	                }
	            }
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
                   load "oc_templates/env.settings"
                      
                    script {
                        
                        openshift.withCluster( 'openshift cluster' ) {
          
                            openshift.withProject("${oc_project}") {
                                                              
                                echo ('Openshift deployment started')
                                //def app = openshift.newApp("'rodexter6/rest-api-sample'","'--as-deployment-config' '--dry-run'")
                                //--IMAGE STREAM-------------------------------------------------------------------------------------------------------------------------------------------------------                                
                                def ispatch = [
                                      "kind": "ImageStream",
                                      "apiVersion": "image.openshift.io/v1",
                                      "metadata": [
                                        "name": "${oc_app_name}",
                                        "namespace": "${oc_project}"
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
                                              "name": "${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/gradle-rest-api-app:${env.GIT_COMMIT}"
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

                                openshift.raw("apply --filename=oc_templates/deploymentConfig.yaml")                               
                                
                                openshift.raw("apply --filename=oc_templates/service.yaml")
                                
                                openshift.raw("apply --filename=oc_templates/route.yaml")
							
								openshift.raw("rollout latest dc/${oc_app_name}")
								echo ('rollout latest dc/${oc_app_name} - done.')	
								
								echo ('Openshift deployment complete!')
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
