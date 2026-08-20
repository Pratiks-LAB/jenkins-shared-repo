def call() {
    stage('Test') {
        echo "Testing application"
        sh 'mvn test'
    }
}
