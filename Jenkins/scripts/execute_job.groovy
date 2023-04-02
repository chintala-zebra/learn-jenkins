import java.io.File 

def setupParams(){
    // Get params from helper
    params_helper = load "Jenkins/libraries/env_params_helper.groovy"
    helperParams = params_helper.getInventoryParamsUptoHost()
    // Add additional params
    jobParams = [
        string( name: 'command_to_execute', 
            description: """
            Please enter the job that needs executed. - <b> Must be full path to the script </b>
            <br> <b>Ex: /mount/jobs/executeme.sh'</b>
            <br> Note: Job will require manual approval if the script is not from <b>/mount or /tmp </b>
            <br> Note: You can't use this to execute <b>rm</b> commands
            """)
    ]
    //setup Parameters to Job
    params_helper.setupParams(helperParams + jobParams)
}

def validateParams(){
    setupParameterDisplay()
    if(params.command_to_execute == "" || params.SERVER == ""){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
    }
    if(params.command_to_execute.contains("rm ")){
        currentBuild.result = 'NOT_BUILT'
        error "You are trying to remove files. This is not allowed."
    }
}

def setupParameterDisplay() {
    addShortText(border: 0, text: "SERVER:" + SERVER, background: "azure", color: "black")
    addShortText(border: 0, text: "Command:" + command_to_execute, background: "beige", color: "black")
}


def validateOptionalParams(){
    if(!params.command_to_execute.contains("/mount") && !params.command_to_execute.contains("/tmp")){
        input "Script file path ${params.command_to_execute} is not in the allowed list. Do you still want to proceed?"
    }
}

def executeJob() {
    ansible_helper = load "Jenkins/libraries/ansible_helper.groovy"
    ansible_helper.setupSSHKeys()

    sh """
        ssh -o 'StrictHostKeyChecking no' -i appadmin-key $SERVER "$command_to_execute"
    """
    log.info ("${params.command_to_execute} execution is successful on ${params.SERVER}")
}

return this
