def processSuccess(){

}

def processError(){

}

def processCleanup(){
    cleanWs()
}

def notifyMiddleware(){
    log.info "Notifying Middleware team of Failure."
}

return this