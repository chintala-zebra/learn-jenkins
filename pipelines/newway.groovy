import groovy.io.FileType


def setupParams(){
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
                                list.add('')
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
                                    dir.eachFile (FileType.DIRECTORIES) { file ->
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