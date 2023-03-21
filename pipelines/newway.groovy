def setupParams(){
    properties([
        parameters([
            [$class: 'ChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Select the Environemnt from the Dropdown List', 
                filterLength: 1, 
                filterable: false, 
                name: 'Env', 
                script: [
                    $class: 'GroovyScript', 
                    fallbackScript: [
                        classpath: [], 
                        sandbox: true, 
                        script: 
                            "return['ERROR']"
                    ], 
                    script: [
                        classpath: [], 
                        sandbox: true, 
                        script: 
                            "return['GCP-Sandbox','GCP-Preprod','GCP-Production']"
                    ]
                ]
            ]
        ])
    ])
}
def planApp() {
     echo "Planning code goes here..."
}
def testApp() {
    echo "Testing code goes here..."
}
def deployApp() {
    echo "Deploy code goes here..."
}

return this