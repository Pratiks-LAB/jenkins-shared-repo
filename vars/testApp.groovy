def call() {
    echo "Starting Maven tests..."

    sh '''
        mvn test
    '''

    echo "Maven tests completed successfully."
}
