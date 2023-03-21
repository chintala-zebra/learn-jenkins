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
                        sandbox: false, 
                        script: 
                            "return['Could not get The environemnts']"
                    ], 
                    script: [
                        classpath: [], 
                        sandbox: false, 
                        script: 
                            "return['dev','stage','prod']"
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