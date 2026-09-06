def call() {
    echo "Deploying to jFrog artifactory...."
  
    configFileProvider([
        configFile(
            fileId: 'MAVEN_SETTINGS',
            variable: 'MAVEN_SETTINGS'
        )
    ]) {

        sh 'mvn deploy -s "$MAVEN_SETTINGS"'

    }
}
