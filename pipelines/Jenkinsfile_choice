properties([
    parameters([
        choice(
            name: 'ENV',
            choices: [
                'dev',
                'prod'
            ]
        ),
        [$class: 'ChoiceParameter',
            choiceType: 'PT_RADIO',
            filterLength: 1,
            filterable: false,
            name: 'CHOICES',
            script: [
                $class: 'GroovyScript',
                fallbackScript: [
                    classpath: [],
                    sandbox: false,
                    script: 'return ["Check Jenkins ScriptApproval page"]'
                ],
                script: [
                    classpath: [],
                    sandbox: false,
                    script: 'return ["One","Two:selected"]'
                ]
            ]
        ]
    ])
])

pipeline {
    agent any

    stages {
        stage('Print the Values') {
            steps {
                echo "Environment: ${params.ENV}"
                echo "Choice: ${params.CHOICES}"
            }
        }
    }
}