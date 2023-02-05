pipeline {
agent { 
        node {
            label 'ansible'
        }
    }
    environment {
        ANSIBLE_VALUT= credentials('MID_ANSIBLE_VAULT')
    }
  stages {
    stage('Decrypt Private Key') {  
      steps {
          sh """
                echo $ANSIBLE_VALUT > .mysecret
                export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
                ansible-vault decrypt the-key
                echo "removed the key" > .mysecret
            """
      }
    }
    stage('Run Play') {  
      steps {
          sh """
               ansible http-servers -i hosts.yaml -m ping
            """
      }
    }
  }
    post {
        always {
            cleanWs()
        }
    }
}