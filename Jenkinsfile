pipeline {
  agent any

  tools {
    maven "MAVEN_HOME"
  }

  stages {

    stage("Deploy to QA") {
      steps {
        echo("deploy to qa")
      }
    }

    stage('Regression Automation Test') {
      steps {
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
          git 'https://github.com/amitmkambli/playwrightNA.git'
          bat "mvn clean test"

        }
      }
    }

    stage('Publish Extent Report') {
      steps {
        publishHTML([allowMissing: false,
          alwaysLinkToLastBuild: false,
          keepAll: true,
          reportDir: 'build',
          reportFiles: 'TestExecutionReport.html',
          reportName: 'HTML Extent Report',
          reportTitles: ''
        ])
      }
    }
  }
}