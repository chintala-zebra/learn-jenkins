
def setupParams(){
    // Nothing to add here
}

def validateParams(){
}

def executeMainteance() {
    log.info("Execuring Maintenance on ${params.SERVER} - ${params.CLUSTER_NAME} - ${params.Application}")
}

return this
