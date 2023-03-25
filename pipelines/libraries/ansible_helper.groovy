def execute_simple_playbook(String inventory, String playbook) {
   sh """
     set +x
     echo $ANSIBLE_VALUT > .mysecret
     export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
     ansible-vault decrypt the-key
     echo "removed the key" > .mysecret

     export ANSIBLE_HOST_KEY_CHECKING=False
     export ANSIBLE_FORCE_COLOR=true
     ansible-playbook -i "$inventory" "$playbook"
    """
}