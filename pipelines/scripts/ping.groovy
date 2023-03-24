
def setupParams(){

}

def validateParams() {
     setupParameterDisplay()
     echo "Validating Parameters code goes here..."
}

def setupParameterDisplay() {
    //addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "azure", color: "black")
}

def executeJob() {
   sh """
     set +x
     echo $ANSIBLE_VALUT > .mysecret
     export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
     ansible-vault decrypt the-key
     echo "removed the key" > .mysecret

     export ANSIBLE_HOST_KEY_CHECKING=False
     export ANSIBLE_FORCE_COLOR=true
     ansible-playbook -i hosts/hosts.yaml ping-play.yaml
    """
}

return this