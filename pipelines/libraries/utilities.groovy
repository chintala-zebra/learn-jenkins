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
            if(params.ENV_TYPE != "" ){
                [name: "Environment", template:  ENV_TYPE],
            }
            if(params.CLUSTER_NAME != "" ){
                [name: "Cluster", template: CLUSTER_NAME],
            }
            if(params.Application != "" ){
                [name: "Application", template: Application]                            
            }
        ]
}

return this