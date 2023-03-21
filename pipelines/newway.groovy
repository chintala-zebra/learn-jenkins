import groovy.io.FileType


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
            ,[$class: 'CascadeChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Select the AMI from the Dropdown List',
                name: 'AMI List', 
                referencedParameters: 'Env', 
                script: 
                    [$class: 'GroovyScript', 
                    
                    script: [
                            classpath: [], 
                            sandbox: true, 
                            script: ''' 
                                import groovy.io.FileType                            
                                def getFoldersUnder(String folderName) {
                                    def list = []
                                    list.add('')
                                    def dir = new File("/inventory/${folderName}/")
                                    dir.eachFileRecurse (FileType.DIRECTORIES) { file ->
                                        list << file.name
                                    }
                                    return list.sort() - 'group_vars' 
                                }
                                return getFoldersUnder(Env)
                            '''
                            
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