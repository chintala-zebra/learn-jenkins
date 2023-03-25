import groovy.io.FileType

def setupParams(ArrayList<> jobParams){
    properties([
        parameters(jobParams)
    ])
}

def getInventoryParamsUptoCluster(){
    clusterParams = [
            [$class: 'ChoiceParameter',
                choiceType: 'PT_SINGLE_SELECT',
                filterable: false,
                name: 'JobName',
                script: [$class: 'GroovyScript',
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                        classpath: [],
                        sandbox: true,
                        script: '''
                            def build = Thread.currentThread().getName()
                            def regexp= ".+?/job/([^/]+)/.*"
                            def match = build  =~ regexp
                            def jobName = match[0][1]
                            def parts = jobName.split('_');
                            return [jobName]
                        '''.stripIndent()
                    ]
                ]
            ]
            ,[$class: 'CascadeChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Select the Environemnt from the Dropdown List', 
                name: 'ENV_TYPE', 
                referencedParameters: 'JobName', 
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
                referencedParameters: 'JobName,ENV_TYPE', 
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
        ]
    return clusterParams
}

def getInventoryParamsUptoApplication(){
    clusterParams = getInventoryParamsUptoCluster()

    applicationParam = [
            [$class: 'CascadeChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Application',
                name: 'Application', 
                referencedParameters: 'JobName,ENV_TYPE,CLUSTER_NAME', 
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
        ]
    allParams = clusterParams + applicationParam
    return allParams
}

def getInventoryParamsUptoHost(){
    applicationParams = getInventoryParamsUptoApplication()
    hostParam = [
        [
                $class: 'CascadeChoiceParameter',
                choiceType: 'PT_SINGLE_SELECT',
                description: '',
                referencedParameters: 'JobName,ENV_TYPE,CLUSTER_NAME,Application',
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
    allParams = applicationParams + hostParam
    return allParams
}

return this