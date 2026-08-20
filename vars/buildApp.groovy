def call() {
    stage('Build') {
        echo "Building the application"
        sh 'mvn clean package -DskipTests'
    }
}
