def showContent() {
    log.info("The file content that you are going to copy is")
    log.info("=============================================================")
    log.info(${params.file})
    log.info("=============================================================")
}


def copyFile() {
    echo $ANSIBLE_VALUT > .mysecret
    export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
    ansible-vault decrypt the-key
    echo "removed the key" > .mysecret
    scp -o 'StrictHostKeyChecking no' -i the-key $file $host_name:$target_file_path
    echo "\033[32m Copy of file to Host : $host_name @ Path : $target_file_path is successful! \033[0m"
}

return this