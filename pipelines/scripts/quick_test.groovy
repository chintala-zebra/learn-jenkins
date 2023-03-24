
def setupParams(String jobName){
    properties([
        parameters([
            [$class: 'ChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Select the Environemnt from the Dropdown List', 
                name: 'ENV_TYPE', 
                script: [
                    $class: 'GroovyScript', 
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                        classpath: [], 
                        sandbox: true, 
                        script: """
                            import groovy.io.FileType                            
                            def getAllFolders() {
                                def list = []
                                def dir = new File("/application/ansible/inventory/")
                                dir.eachFile (FileType.DIRECTORIES) { file ->
                                    list << file.name
                                }
                                return list.sort() - 'group_vars' 
                            }
                            def name = "$jobName"
                            def parts = name.split('_');
                            if(parts.length > 2){
                                return [parts[2]]
                            } else {
                                return getAllFolders()
                            }
                        """   
                    ]
                ]
            ]
        ])
    ])
}

def validateParams() {
    setupParameterDisplay()
     echo "Validating Parameters code goes here..."
     if(false){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
     }
}

def setupParameterDisplay() {
    //addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "azure", color: "black")
    //addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "beige", color: "black")
}

def validateOptionalParams(){
    echo "Validating Optional Parameters code goes here..."
}

def executeJob(String jobName) {
    echo jobName
    sh """
        echo $jobName
    """
    sh '''
        echo $jobName
    '''
    echo "Execute Job code goes here..."
}

return this