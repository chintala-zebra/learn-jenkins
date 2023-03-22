
def setupParams(){
        existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }

    jobParams = existing +     [
            $class: 'CascadeChoiceParameter',
            choiceType: 'PT_SINGLE_SELECT',
            referencedParameters: 'SNOWTICKET, Application',
            description: """
            <b>wasCheckProcess</b> - Check how many processes are running on Cluster. <br>
            <b>appHealth</b> - Check JVM's/Web health. <br>
            <b>wasBiweeklyMaintenance</b> - Application recycles with/without deployment. <br>
            <b>wasBiweeklyMaintenanceSTOP</b>- Application stop with/without deployment.<br>
            <b>wasBiweeklyMaintenanceSTART</b>- Start the application environment.<br>
            <b>rollingRestartServers</b>- Rolling restarts for the application CLUSTER<br>
            """,
            name: 'JOB_NAME',
            script: [
                $class: 'GroovyScript',
                fallbackScript: [ classpath: [], sandbox: true, script: 'return ["ERROR"]' ],
                script: [
                    classpath: [],
                    sandbox: true,
                    script: '''
                        if (Application.isEmpty() || Application==""){
                            return ''
                        }
                        else
                        {                        
                            def jobs = [
                                "appHealth",
                                "wasCheckProcess",
                                "wasBiweeklyMaintenance",
                                "wasBiweeklyMaintenanceSTOP",
                                "wasBiweeklyMaintenanceSTART",
                                "RWS_Extra_Static_Deployment",
                                "rollingRestartServers"
                            ]
                            return jobs
                        }
                    '''
                ]
            ]
        ]

    properties([
        parameters(jobParams)
    ])
}

def validateParams(){
    if(params.ENV_TYPE == null || params.ENV_TYPE == "" || params.CLUSTER_NAME == null || params.CLUSTER_NAME == "" || params.Application == null || params.Application == ""){
        currentBuild.result = 'FAILURE'
        error "Required Parameters are empty so, skipping execution."
    }
}

def executeMainteance() {
    log.info("Execuring Maintenance on ${params.ENV_TYPE} - ${params.CLUSTER_NAME} - ${params.Application}")
}

return this
