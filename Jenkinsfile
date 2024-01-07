pipeline {
    agent any
    environment {
        dockerImageName = 'alvonellos/communicator-api'
        // Define your Docker image name
        kubernetesNamespace = 'communicator-api' // Update with your Kubernetes namespace
        kubeconfig = credentials('cluster') // Use the ID of the Kubernetes credential
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                sh 'mvn -B -DskipTests clean install -U'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing...'
                sh 'mvn test'
            }
        }

        stage('Build image') {
            steps {
                echo 'Building Docker image...'
                script {
                    def dockerImage = docker.build("${dockerImageName}:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Push image to Docker Hub') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                script {
                    docker.withRegistry('https://registry-1.docker.io', '65fbcb2c-d252-4e31-bacc-8e63f06f3d87') {
                        // Use the ID of the Jenkins credentials that store Docker Hub username and password
                        def dockerImage = docker.image("${dockerImageName}:${env.BUILD_NUMBER}")
                        dockerImage.push()
                    }
                }
            }
        }


        stage('Check Kubernetes') {
            steps {
                echo 'Getting kubectl'
                script {
                    sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'
                    sh 'chmod u+x ./kubectl'
                    sh './kubectl get pods'
                    sh "kubectl version"
                    sh "kubectl config view"
                }
            }
        }

        stage('Delete Old Namespace') {
            steps {
                script {
                    sh "kubectl delete namespace ${kubernetesNamespace} --ignore-not-found=true"
                }
            }
        }

       stage('Create New Namespace') {
            steps {
                script {
                    sh "kubectl create namespace ${kubernetesNamespace} --dry-run=client -o yaml | kubectl apply -f -"
                }
            }
        }

        stage('Copy vault secret') {
            steps {
                script {
                    sh "kubectl get secret vault --namespace=default -oyaml | grep -v '^\\s*namespace:\\s' | kubectl apply --namespace=${kubernetesNamespace} -f -"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "export BUILD_NUMBER=${env.BUILD_NUMBER}"
                    sh "cat kubernetes/deployment.yaml | envsubst | kubectl apply -f - -n ${kubernetesNamespace}"
                    sh "kubectl apply -f kubernetes/service.yaml -n ${kubernetesNamespace}"
                }
            }
        }
    }
}
