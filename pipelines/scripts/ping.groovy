
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
    ansible_helper = load "pipelines/libraries/ansible_helper.groovy"
    ansible_helper.execute_simple_playbook("hosts/hosts.yaml","ping-play.yaml")
}

return this