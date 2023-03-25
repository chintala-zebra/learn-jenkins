def processSuccess(){

}

def processError(){

}

def processCleanup(){
    cleanWs()
}

def notifyMiddleware(){
    log.info "Notifying Middleware team of Failure."
    office365ConnectorSend webhookUrl: MIDDLEWARE_TEAMS_CHANNEL,
        factDefinitions: [
                [name: "Environment", template:  ENV_TYPE],
                [name: "Cluster", template: CLUSTER_NAME],
                [name: "Application", template: Application]                            
        ]
}

return this