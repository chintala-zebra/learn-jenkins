
def setupParams(){
    echo "Additioanl Parameters code goes here..."
}
def validateParams() {
     echo "Validating Parameters code goes here..."
     if(false){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
     }
}
def executeJob() {
    echo "Execute Job code goes here..."
}

return this