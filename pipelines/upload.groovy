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
                    sh '''
                            set +x
                            echo $ANSIBLE_VALUT > .mysecret
                            export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
                            ansible-vault decrypt the-key
                            echo "removed the key" > .mysecret
                            scp -o 'StrictHostKeyChecking no' -i the-key $file $host_name:$target_file_path
                            echo "\033[32m Copy of file to Host : $host_name @ Path : $target_file_path is successful! \033[0m"
                    '''
}

return this
