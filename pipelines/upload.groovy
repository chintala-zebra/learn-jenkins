import java.io.File 

def showContent() {
    log.info("The file content that you are going to copy is")
    log.info("=============================================================")
    def content = base64Decode (params.file)
    writeFile(file: "fileName", text: content)
    sh '''
    if [[ `du -k "${WORKSPACE}/fileName" | cut -f1` -lt 20 ]]; then
        cat ${WORKSPACE}/fileName
    else
        echo "file is too large"
    fi
    '''
    log.info("=============================================================")
}

def base64Decode(encodedString){
    byte[] decoded = encodedString.decodeBase64()
    String decode = new String(decoded)
    return decode
}

def copyFile() {
   sh 'echo "${env.ANSIBLE_VALUT}" > .mysecret'
}

return this
