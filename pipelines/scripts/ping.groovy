
def setupParams(){
    // Get params from helper
    params_helper = load "pipelines/libraries/env_params_helper.groovy"
    helperParams = params_helper.getInventoryParamsUptoApplication()
    //log.info("Additioanl Parameters code goes here...")
    // Add additional params
    //jobParams = [
    //]
    //setup Parameters to Job
    params_helper.setupParams(helperParams)
}

def validateParams() {
    setupParameterDisplay()
    if(params.ENV_TYPE == "" || params.CLUSTER_NAME == "" || params.Application == "" || params.JOB_NAME == ""){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
    }
}

def setupParameterDisplay() {
    if(params.ENV_TYPE != "" ){
        addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "azure", color: "black")
    }
    if(params.CLUSTER_NAME != "" ){
        addShortText(border: 0, text: "CLUSTER_NAME:" + CLUSTER_NAME , background: "beige", color: "black")
    }
    if(params.Application != "" ){
        addShortText(border: 0, text: "Application:" + Application, background: "azure", color: "black")
    }
}

def executeJob() {
    ansible_helper = load "pipelines/libraries/ansible_helper.groovy"
    ansible_helper.execute_simple_playbook("/application/ansible/inventory/${ENV_TYPE}/${CLUSTER_NAME}/${Application}","ping-play.yaml")
}

return this