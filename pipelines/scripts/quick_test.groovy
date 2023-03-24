
def setupParams(){
    echo "Additioanl Parameters code goes here..."
}

def validateParams() {
    setupParameterDisplay()
     echo "Validating Parameters code goes here..."
     if(false){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
     }
}

def setupParameterDisplay() {
    //addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "azure", color: "black")
    //addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "beige", color: "black")
}

def validateOptionalParams(){
    echo "Validating Optional Parameters code goes here..."
}

def executeJob(String jobName) {
    echo jobName
    def jobName1 = jobName
    sh """
        echo jobName1
    """
    echo "Execute Job code goes here..."
}

return this