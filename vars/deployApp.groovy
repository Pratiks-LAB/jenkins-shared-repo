def call() {

    echo "Starting deployment..."
    echo "Deploying application to Artifactory..."

    configFileProvider([
        configFile(
            fileId: '40fb55ba-2143-4530-addb-4f7590640fa6',
            variable: 'MAVEN_SETTINGS'
        )
    ]) {
        sh '''
            set -e
            mvn deploy -s "$MAVEN_SETTINGS"
        '''
    }

    echo "WAR deployed to Artifactory successfully."
}
