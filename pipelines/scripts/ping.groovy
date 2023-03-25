
def setupParams(){
    // Get params from helper
    //params_helper = load "pipelines/libraries/env_params_helper.groovy"
    //helperParams = params_helper.getInventoryParamsUptoHost()
    //log.info("Additioanl Parameters code goes here...")
    // Add additional params
    //jobParams = [
    //]
    //setup Parameters to Job
    //params_helper.setupParams(helperParams + jobParams)
}

def validateParams() {
     //setupParameterDisplay()
     //log.info("Validating Parameters code goes here...")
}

def validateOptionalParams(){
    
}

def setupParameterDisplay() {

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