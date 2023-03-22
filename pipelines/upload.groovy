import java.io.File 

def setupParams(){
    existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }

    // Create new params and merge them with existing ones
    jobParams = existing + [
        base64File (name: 'file', description: "File to Upload"),
        string( name: 'target_file_path', 
                     description: """
                     Path to where the file needs copied - <b> Must Include the file name </b>
                     <br> Ex: /mount/test_folder/abcd.xml'
                     """)
    ] 
    // Create properties
    properties([
        parameters(jobParams)
    ])
}

def showContent() {
    if(areRequiredParamsSet()){
        log.info "Required Parameters are empty so, skipping execution."
    } else {
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
}

def base64Decode(encodedString){
    byte[] decoded = encodedString.decodeBase64()
    String decode = new String(decoded)
    return decode
}

def copyFile() {
    if(areRequiredParamsSet()){
        log.info "Required Parameters are empty so, skipping execution."
        //currentBuild.result = 'FAILURE'
    } else {
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
}

def areRequiredParamsSet(){
    if(params.target_file_path == "" || params.file == "" || params.SERVER == ""){
        return false
    } else {
        return true
    }

}

return this
