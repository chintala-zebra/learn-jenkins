import java.io.File 

def setupParams(){
    params_helper = load "pipelines/libraries/env_params_helper.groovy"
    hostParams = params_helper.getInventoryParamsUptoHost()

    jobParams = [
        string( name: 'command_to_execute', 
            description: """
            Please enter the job that needs executed. - <b> Must be full path to the script </b>
            <br> <b>Ex: /mount/jobs/executeme.sh'</b>
            <br> Note: Job will require manual approval if the script is not from <b>/mount or /tmp </b>
            <br> Note: You can't use this to execute <b>rm</b> commands
            """)
    ]
    properties([
        parameters(hostParams + jobParams)
    ])
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
        input "File path is not in allowed list. Do you still want to proceed?"
    }
}

def executeJob() {
    sh '''
        set +x
        echo $ANSIBLE_VALUT > .mysecret
        export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
        ansible-vault decrypt the-key
        echo "removed the key" > .mysecret
        ssh -o 'StrictHostKeyChecking no' -i the-key $SERVER "$command_to_execute"
    '''
    log.info ("Job execution is successful!")
}

return this
