import groovy.io.FileType


@NonCPS
def call(String folderName) {
    def list = []
    list.add('')
    def dir = new File("/inventory/${folderName}/")
    dir.eachFileRecurse (FileType.DIRECTORIES) { file ->
        list << file.name
    }
    return list.sort() - 'group_vars' 
}

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
                    fallbackScript: [
                            classpath: [], 
                            sandbox: true, 
                            script: "return['Could not get Environment from Env Param']"
                            ], 
                    script: [
                            classpath: [], 
                            sandbox: true, 
                            script: '''
                            if (Env.equals("GCP-Sandbox")){
                                return["ami-sd2345sd", "ami-asdf245sdf", "ami-asdf3245sd"]
                            }
                            else if(Env.equals("GCP-Preprod")){
                                return["ami-sd34sdf", "ami-sdf345sdc", "ami-sdf34sdf"]
                            }
                            else if(Env.equals("GCP-Production")){
                                return["ami-sdf34sdf", "ami-sdf34ds", "ami-sdf3sf3"]
                            }
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