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
        			load "oc_templates/env.settings"
        			
        			oc_app_name = (oc_app_name + env.BRANCH_NAME).replaceAll("feature/", "-")
   
	                docker.withRegistry("https://${NEXUS_HOST}:${NEXUS_PORT}", 'nexusOssCredentials') {
	                	def customImage = docker.build("${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/${oc_app_name}:latest")
	                    customImage.push()
	                }
	            }
            }
        }

        stage('Deploy') {
                   steps {
                                      
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
                                              "name": "${NEXUS_HOST}:${NEXUS_PORT}/repository/docker-hosted/${oc_app_name}:latest"
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

								sh 'sed -i "s/{{oc_project}}/'+"${oc_project}"+'/g" oc_templates/deploymentConfig.yaml'
								sh 'sed -i "s/{{oc_app_name}}/'+"${oc_app_name}"+'/g" oc_templates/deploymentConfig.yaml'
                                
                                openshift.raw("apply --filename=oc_templates/deploymentConfig.yaml") 
								
								sh 'sed -i "s/{{oc_project}}/'+"${oc_project}"+'/g" oc_templates/route.yaml'
								sh 'sed -i "s/{{oc_app_name}}/'+"${oc_app_name}"+'/g" oc_templates/route.yaml'

     							openshift.raw("apply --filename=oc_templates/route.yaml")
							
								sh 'sed -i "s/{{oc_project}}/'+"${oc_project}"+'/g" oc_templates/service.yaml'
								sh 'sed -i "s/{{oc_app_name}}/'+"${oc_app_name}"+'/g" oc_templates/service.yaml'
	                           
	                            openshift.raw("apply --filename=oc_templates/service.yaml")													
                                
								openshift.raw("rollout latest dc/"+"${oc_app_name}")
								echo ('rollout latest dc/'+"${oc_app_name}"+' - done.')	
								
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
