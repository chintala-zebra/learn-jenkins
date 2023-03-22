
def setupParams(){
    // Nothing to add here
}

def validateParams(){
    if(params.ENV_TYPE == null || params.ENV_TYPE == "" || params.CLUSTER_NAME == null || params.CLUSTER_NAME == "" || params.Application == null ||  || params.Application == ""){
        currentBuild.result = 'FAILURE'
        error "Required Parameters are empty so, skipping execution."
    }
}

def executeMainteance() {
    log.info("Execuring Maintenance on ${params.ENV_TYPE} - ${params.CLUSTER_NAME} - ${params.Application}")
}

return this
