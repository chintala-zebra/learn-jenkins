import java.io.File 

def setupParams(){
    existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }

    jobParams = existing + [
        string( name: 'command_to_execute', 
            description: """
            Please enter the job that needs executed. - <b> Must be full path to the script </b>
            <br> Ex: /mount/jobs/executeme.sh'
            <br> Note: Job will require manual approval if the script is not from <b>/mount or /tmp </b>
            """)
    ]
    properties([
        parameters(jobParams)
    ])
}

def validateParams(){
    if(params.command_to_execute == "" || params.SERVER == ""){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
    }
}

def validateFilePath(){
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
