def setupSSHKeys() {
    sh """
        set +x
        echo $ANSIBLE_VALUT > .mysecret
        export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
        ansible-vault decrypt appadmin-key
        echo "removed the key" > .mysecret
    """
}

def execute_playbook(String inventory, String playbook) {
    execute_playbook_on_host(inventory,"*",playbook)
}

def execute_playbook_on_host(String inventory, String hostNames, String playbook) {
    log.info("Executing Playbook ${playbook} on inventory ${inventory} with host(s) ${hostNames}")
    setupSSHKeys()
    sh """
        set +x
        export ANSIBLE_HOST_KEY_CHECKING=False
        export ANSIBLE_FORCE_COLOR=true
        ansible-playbook -i "${inventory}" -e "HOSTS=${hostNames}" "${playbook}" --private-key appadmin-key
    """
    log.info("Playbook ${playbook} execution on inventory ${inventory} with host(s) ${hostNames} completed successfully.")
}

return this