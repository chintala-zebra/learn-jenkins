
def setupParams(){
    // Get params from helper
    params_helper = load "Jenkins/libraries/env_params_helper.groovy"
    helperParams = params_helper.getInventoryParamsUptoHostAsMultiSelect()
    //log.info("Additioanl Parameters code goes here...")
    // Add additional params
    jobParams = [
        choice(name: 'PLAY_BOOK', choices: ['whoami-playbook.yml'], description: 'Playbook to Execute')
    ]
    //setup Parameters to Job
    params_helper.setupParams(helperParams + jobParams)
}

def validateParams() {
    setupParameterDisplay()
    if(params.ENV_TYPE == "" || params.CLUSTER_NAME == "" || params.Application == "" || params.SERVER == "" || params.PLAY_BOOK == ""){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
    }
}

def setupParameterDisplay() {
    if(params.ENV_TYPE != "" ){
        addShortText(border: 0, text: "Environment:" + ENV_TYPE, background: "azure", color: "black")
    }
    if(params.CLUSTER_NAME != "" ){
        addShortText(border: 0, text: "Cluster:" + CLUSTER_NAME , background: "beige", color: "black")
    }
    if(params.Application != "" ){
        addShortText(border: 0, text: "Application:" + Application, background: "azure", color: "black")
    }
    if(params.SERVER != "" ){
        addShortText(border: 0, text: "SERVER:" + SERVER, background: "azure", color: "black")
    }
    if(params.PLAY_BOOK != "" ){
        addShortText(border: 0, text: "Play Book:" + PLAY_BOOK, background: "azure", color: "black")
    }
}

def executeJob() {
    ansible_helper = load "Jenkins/libraries/ansible_helper.groovy"
    ansible_helper.execute_playbook_on_host("/application/ansible/inventory/${ENV_TYPE}/${CLUSTER_NAME}/${Application}","${SERVER}","${WORKSPACE}/Ansible/${PLAY_BOOK}")
}

return this