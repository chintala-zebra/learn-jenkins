
def setupParams(){
    // Get params from helper
    params_helper = load "pipelines/libraries/env_params_helper.groovy"
    //helperParams = params_helper.getInventoryParamsUptoHost()
    log.info("Additioanl Parameters code goes here...")
    // Add additional params
    //jobParams = [
    //]
    //setup Parameters to Job
    //params_helper.setupParams(helperParams + jobParams)

}

def validateParams() {
    setupParameterDisplay()
     log.info("Validating Parameters code goes here...")
     if(false){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
     }
}

def setupParameterDisplay() {
    // if(params.ENV_TYPE != "" ){
    //     addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "azure", color: "black")
    // }
}

def validateOptionalParams(){
    log.info("Validating Optional Parameters code goes here...")
}

def executeJob() {
    log.info("Execute Job code goes here...")
}

return this