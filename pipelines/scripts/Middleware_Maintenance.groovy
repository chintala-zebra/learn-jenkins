
def setupParams(){
        existing = currentBuild.rawBuild.parent.properties
    .findAll { it.value instanceof hudson.model.ParametersDefinitionProperty }
    .collectMany { it.value.parameterDefinitions }
    jobParams = [
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
    allParams = existing +  jobParams
    properties([
        parameters(allParams)
    ])
}

def validateParams(){
    setupParameterDisplay()
    if(params.ENV_TYPE == "" || params.CLUSTER_NAME == "" || params.Application == "" || params.JOB_NAME == ""){
        currentBuild.result = 'NOT_BUILT'
        error "Required Parameters are empty so, skipping execution."
    }
}

def validateOptionalParams(){
   // log.info("Validating Optional Parameters code goes here...")
}

def setupParameterDisplay() {
    addShortText(border: 0, text: "NEW_VAR:" + CLUSTER_NAME + "_" + ENV_TYPE, background: "beige", color: "black")
    addShortText(border: 0, text: "ENVIRONMENT:" + ENV_TYPE, background: "azure", color: "black")
    addShortText(border: 0, text: "CLUSTER_NAME:" + CLUSTER_NAME , background: "beige", color: "black")
    addShortText(border: 0, text: "Application:" + Application, background: "azure", color: "black")
    addShortText(border: 0, text: "JOB_NAME:" + JOB_NAME, background: "beige", color: "black")
}

def executeJob() {
    log.info("Execuring Maintenance on ${params.ENV_TYPE} - ${params.CLUSTER_NAME} - ${params.Application} - ${params.JOB_NAME}")
}

return this
