
def setupParams(){

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

def executeJob() {
    echo jobName
    echo "Execute Job code goes here..."
}

return this