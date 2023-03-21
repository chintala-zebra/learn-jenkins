def setupParams(){
    properties([
        parameters([
            choice(
                choices: ['ONE', 'TWO'], 
                name: 'PARAMETER_01'
            )
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