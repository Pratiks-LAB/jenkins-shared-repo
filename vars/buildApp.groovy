def call() {
    echo "Starting Maven build..."

    sh '''
        mvn clean package -DskipTests
    '''

    echo "Maven build completed successfully."
}
