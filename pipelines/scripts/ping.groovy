
def setupParams(){

}
def validateParams() {
     echo "Validating Parameters code goes here..."
}
def executeJob() {
   sh """
     set +x
     echo $ANSIBLE_VALUT > .mysecret
     export ANSIBLE_VAULT_PASSWORD_FILE=.mysecret
     ansible-vault decrypt the-key
     echo "removed the key" > .mysecret

     export ANSIBLE_HOST_KEY_CHECKING=False
     export ANSIBLE_FORCE_COLOR=true
     ansible-playbook -i hosts/hosts.yaml ping-play.yaml
    """
}

return this