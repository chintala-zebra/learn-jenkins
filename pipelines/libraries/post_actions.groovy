def processCleanup(){
    cleanWs()
}

def processError(){
    log.info "Notifying Middleware team of Failure."
}

return this