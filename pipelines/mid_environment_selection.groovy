import groovy.io.FileType

def setupEnvParams(){
    properties([
        parameters([
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
                name: 'Env', 
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
                                def dir = new File("/inventory/")
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
                description: 'Cluster',
                name: 'Cluster', 
                referencedParameters: 'JobName,Env', 
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
                                    def dir = new File("/inventory/${folderName}/")
                                    dir.eachFile (FileType.DIRECTORIES) { file ->
                                        list << file.name
                                    }
                                    return list.sort() - 'group_vars' 
                                }
                                def parts = JobName.split('_');
                                if(parts.length > 3){
                                    return [parts[3]]
                                } else {
                                    return getFoldersUnder(Env)
                                } 
                            '''
                            
                        ]
                ]
            ]
            ,[$class: 'CascadeChoiceParameter', 
                choiceType: 'PT_SINGLE_SELECT', 
                description: 'Application',
                name: 'Application', 
                referencedParameters: 'JobName,Env,Cluster', 
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
                                    def dir = new File("/inventory/${folderName}/")
                                    dir.eachFile (FileType.FILES) { file ->
                                        list << file.name.replaceAll('.yml','');
                                    }
                                    return list  - null - ''
                                }
                                def parts = JobName.split('_');
                                if(parts.length > 4){
                                    return [parts[4]]
                                } else {
                                    return getFoldersUnder("${Env}/${Cluster}")
                                } 
                            '''
                            
                        ]
                ]
            ]
            ,[
                $class: 'CascadeChoiceParameter',
                choiceType: 'PT_SINGLE_SELECT',
                description: '',
                referencedParameters: 'JobName,Env,Cluster,Application',
                name: 'SERVER',
                script: [
                    $class: 'GroovyScript',
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                        classpath: [],
                        sandbox: true,
                        script:  
                        '''
                            def command = ['/bin/sh',  '-c',  "cat /inventory/${Env}/${Cluster}/${Application}|grep z182|sed 's/^ *//g;s/://g'|sort -u "]
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
        ])
    ])
}

def setupHostParams(){
    setupEnvParams()

    existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }

    // Create new params and merge them with existing ones
    jobParams = existing + [
        [
                $class: 'CascadeChoiceParameter',
                choiceType: 'PT_SINGLE_SELECT',
                description: '',
                referencedParameters: 'JobName,Env,Cluster,Application',
                name: 'SERVER',
                script: [
                    $class: 'GroovyScript',
                    fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                    script: [
                        classpath: [],
                        sandbox: true,
                        script:  
                        '''
                            def command = ['/bin/sh',  '-c',  "cat /inventory/${Env}/${Cluster}/${Application}|grep z182|sed 's/^ *//g;s/://g'|sort -u "]
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


return this