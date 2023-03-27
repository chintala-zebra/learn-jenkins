def setupSSHKeys() {
    sh """
        set +x
        echo $ANSIBLE_VALUT > .mysecret
        export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
        ansible-vault decrypt appadmin-key
        echo "removed the key" > .mysecret
    """
}

def execute_simple_playbook(String inventory, String playbook) {
    log.info("Executing Playbook ${playbook} on inventory ${inventory}")
    setupSSHKeys()
    sh """
        set +x
        export ANSIBLE_HOST_KEY_CHECKING=False
        export ANSIBLE_FORCE_COLOR=true
        ansible-playbook -i "${inventory}" "${playbook}" --private-key appadmin-key
    """
    log.info("Playbook ${playbook} execution on inventory ${inventory} completed successfully.")
}

def execute_simple_playbook_on_host(String inventory, String hostName, String playbook) {
    log.info("Executing Playbook ${playbook} on host ${hostName}")
    setupSSHKeys()
    sh """
        set +x
        export ANSIBLE_HOST_KEY_CHECKING=False
        export ANSIBLE_FORCE_COLOR=true
        ansible-playbook -i "${inventory}" -e "HOSTS=${hostName}" "${playbook}" --private-key appadmin-key
    """
    log.info("Playbook ${playbook} execution on host ${hostName} completed successfully.")
}

return this