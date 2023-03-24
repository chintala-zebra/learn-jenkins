import groovy.io.FileType

def addInventoryParamsUptoApplication(String jobName){
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
                        script: '''
                            import groovy.io.FileType                            
                            def getAllFolders() {
                                def list = []
                                def dir = new File("/application/ansible/inventory/")
                                dir.eachFile (FileType.DIRECTORIES) { file ->
                                    list << file.name
                                }
                                return list.sort() - 'group_vars' 
                            }
                            def parts = JobName.split('_');
                            if(parts.length > 2){
                                return [parts[2]]
                            } else {
                                return getAllFolders()
                            }
                        '''
                            
                    ]
                ]
            ]
            ,[$class: 'CascadeChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'CLUSTER_NAME',
                name: 'CLUSTER_NAME', 
                referencedParameters: 'ENV_TYPE', 
                script: 
                    [$class: 'GroovyScript', 
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                            classpath: [], 
                            sandbox: true, 
                            script: ''' 
                                import groovy.io.FileType                            
                                def getFoldersUnder(String folderName) {
                                    def list = []
                                    list.add('')
                                    def dir = new File("/application/ansible/inventory/${folderName}/")
                                    dir.eachFile (FileType.DIRECTORIES) { file ->
                                        list << file.name
                                    }
                                    return list.sort() - 'group_vars' 
                                }
                                def parts = JobName.split('_');
                                if(parts.length > 3){
                                    return [parts[3]]
                                } else {
                                    return getFoldersUnder(ENV_TYPE)
                                } 
                            '''
                            
                        ]
                ]
            ]
            ,[$class: 'CascadeChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Application',
                name: 'Application', 
                referencedParameters: 'ENV_TYPE,CLUSTER_NAME', 
                script: 
                    [$class: 'GroovyScript', 
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                            classpath: [], 
                            sandbox: true, 
                            script: ''' 
                                import groovy.io.FileType                            
                                def getFoldersUnder(String folderName) {
                                    def list = []
                                    list.add('')
                                    def dir = new File("/application/ansible/inventory/${folderName}/")
                                    dir.eachFile (FileType.FILES) { file ->
                                        list << file.name.replaceAll('.yml','');
                                    }
                                    return list  - null - ''
                                }
                                def parts = JobName.split('_');
                                if(parts.length > 4){
                                    return [parts[4]]
                                } else {
                                    return getFoldersUnder("${ENV_TYPE}/${CLUSTER_NAME}")
                                } 
                            '''
                            
                        ]
                ]
            ]
        ])
    ])
}

def addInventoryParamsUptoHost(String jobName){
    addInventoryParamsUptoApplication(jobName)

    existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }

    // Create new params and merge them with existing ones
    jobParams = existing + [
        [
                $class: 'CascadeChoiceParameter',
                choiceType: 'PT_SINGLE_SELECT',
                description: '',
                referencedParameters: 'ENV_TYPE,CLUSTER_NAME,Application',
                name: 'SERVER',
                script: [
                    $class: 'GroovyScript',
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                        classpath: [],
                        sandbox: true,
                        script:  
                        '''
                            def command = ['/bin/sh',  '-c',  "cat /application/ansible/inventory/${ENV_TYPE}/${CLUSTER_NAME}/${Application}|grep z182|sed 's/^ *//g;s/://g'|sort -u "]
                            def proc = command.execute()
                            proc.waitFor()              
                            def output = proc.in.text
                            def exitcode= proc.exitValue()
                            def error = proc.err.text
                            if (error) {
                                return "ERROR"
                            }
                            else
                            {
                            return output.tokenize()
                            }
                            
                        '''
                    ]
                ]
            ]
    ] 
    // Create properties
    properties([
        parameters(jobParams)
    ])
}


def performPostActions(){
    cleanWs()
}

return this