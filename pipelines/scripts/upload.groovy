import java.io.File 

def setupParams(){
    existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }

    jobParams = existing + [
        base64File (name: 'file', description: "File to Upload"),
        string( name: 'target_file_path', 
                     description: """
                     Path to where the file needs copied - <b> Must Include the file name </b>
                     <br> Ex: /mount/test_folder/abcd.xml'
                     """)
    ] 

    properties([
        parameters(jobParams)
    ])
}

def validateParams(){
    setupParameterDisplay()
    if(params.target_file_path == "" || params.file == "" || params.SERVER == ""){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
    }
}

def setupParameterDisplay() {
    addShortText(border: 0, text: "SERVER:" + SERVER, background: "azure", color: "black")
    addShortText(border: 0, text: "File Path:" + target_file_path, background: "beige", color: "black")
}

def validateFilePath(){
    if(!params.target_file_path.contains("/tmp")){
        input "File path is not in allowed list. Do you still want to proceed?"
    }
}

def base64Decode(encodedString){
    byte[] decoded = encodedString.decodeBase64()
    String decode = new String(decoded)
    return decode
}

def showContent() {
    log.info("The file content that you are going to copy is")
    log.info("=============================================================")
    def content = base64Decode (params.file)
    writeFile(file: "fileName", text: content)
    sh '''
        set +x
        if [[ `du -k "${WORKSPACE}/fileName" | cut -f1` -lt 20 ]]; then
            cat ${WORKSPACE}/fileName
        else
            echo "file is too large"
        fi
    '''
    log.info("=============================================================")
}

def copyFile() {
    sh '''
        set +x
        echo $ANSIBLE_VALUT > .mysecret
        export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
        ansible-vault decrypt the-key
        echo "removed the key" > .mysecret
        scp -o 'StrictHostKeyChecking no' -i the-key ${WORKSPACE}/fileName $SERVER:$target_file_path
    '''
    log.info ("Copy of file to Host : ${params.SERVER} @ Path : ${params.target_file_path} is successful!")
}

return this
