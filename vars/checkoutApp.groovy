def call(){
  stage('Checkout') {
    steps {
      git branch: 'main',
        credentialsId: '1f418500-b4c7-43ae-83ba-2e48f4342da3',
        url: 'https://github.com/Pratiks-LAB/sample-webapp.git'
    }
  }
}
