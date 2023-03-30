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
    execute_playbook_on_host_with_vars(inventory, "*", playbook, "")
}

def execute_playbook_with_vars(String inventory, String playbook, String extraVars) {
    execute_playbook_on_host_with_vars(inventory,"*", playbook, extraVars)
}

def execute_playbook_on_host(String inventory, String hostNames, String playbook) {
    execute_playbook_on_host_with_vars(inventory, hostNames, playbook, "")
}

def execute_playbook_on_host_with_vars(String inventory, String hostNames, String playbook, String extraVars) {
    log.info("Executing Playbook ${playbook} on inventory ${inventory} with host(s) ${hostNames} and extravars ${extraVars}")
    setupSSHKeys()
    sh """
        set +x
        export ANSIBLE_HOST_KEY_CHECKING=False
        export ANSIBLE_FORCE_COLOR=true
        if(extraVars == ""){
            ansible-playbook -i "${inventory}" -e "HOSTS=${hostNames}" "${playbook}" --private-key appadmin-key
        } else {
            ansible-playbook -i "${inventory}" -e "HOSTS=${hostNames}" "${playbook}" --extra-vars="${extraVars}" --private-key appadmin-key 
        }
    """
    log.info("Playbook ${playbook} execution on inventory ${inventory} with host(s) ${hostNames} and extravars ${extraVars} completed successfully.")
}

return this