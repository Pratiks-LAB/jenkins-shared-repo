def call() {

    echo "Starting deployment..."

    // 1. Deploy WAR to Artifactory
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


    // 2. Find latest SNAPSHOT build from Artifactory and download it
    withCredentials([
        usernamePassword(
            credentialsId: 'jfrog_pass',
            usernameVariable: 'ART_USER',
            passwordVariable: 'ART_PASS'
        )
    ]) {

        sh '''
            set -e

            BASE_URL="http://18.212.158.106:8081/artifactory/libs-snapshot-local"
            GROUP_PATH="com/example/sample-webapp"
            ARTIFACT_ID="sample-webapp"
            VERSION="1.1-SNAPSHOT"

            # Find latest SNAPSHOT build
            METADATA_URL="$BASE_URL/$GROUP_PATH/$VERSION/maven-metadata.xml"

            echo "Finding latest SNAPSHOT build from Artifactory..."

            curl -u "$ART_USER:$ART_PASS" \
                 -s "$METADATA_URL" \
                 -o metadata.xml

            TIMESTAMP=$(grep -oPm1 '(?<=<timestamp>)[^<]+' metadata.xml)
            BUILDNUM=$(grep -oPm1 '(?<=<buildNumber>)[^<]+' metadata.xml)

            WAR_NAME="${ARTIFACT_ID}-1.1-${TIMESTAMP}-${BUILDNUM}.war"

            ARTIFACT_URL="$BASE_URL/$GROUP_PATH/$VERSION/$WAR_NAME"

            echo "Latest WAR found: $WAR_NAME"

            # Download latest WAR
            echo "Downloading latest WAR from Artifactory..."

            curl -u "$ART_USER:$ART_PASS" \
                 -o "/tmp/$WAR_NAME" \
                 "$ARTIFACT_URL"


            echo "Stopping Tomcat..."

            sudo /opt/tomcat/tomcat-10/bin/shutdown.sh || true

            echo "Removing old WAR..."

            sudo rm -f /opt/tomcat/tomcat-10/webapps/sample-webapp.war

            echo "Copying downloaded WAR..."

            sudo cp "/tmp/$WAR_NAME" \
                    /opt/tomcat/tomcat-10/webapps/sample-webapp.war

            echo "Starting Tomcat..."

            sudo /opt/tomcat/tomcat-10/bin/startup.sh

            echo "Deployment completed successfully."

            echo "Deployed WAR: $WAR_NAME"
        '''
    }
}
