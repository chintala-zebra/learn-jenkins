def showContent() {
    log.info("The file content that you are going to copy is")
    log.info("=============================================================")
    log.info(${params.file})
    log.info("=============================================================")
}


def copyFile() {
    echo ${env.ANSIBLE_VALUT} > .mysecret
}

return this