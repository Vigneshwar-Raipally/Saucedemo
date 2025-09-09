pipeline {
    agent any

    environment {
        BRANCH_NAME = 'main'
        ECLIPSE_WORKSPACE = 'C:\\Users\\datta\\eclipse-workspace\\Test-Automation-SwagLabs'
        COMMIT_MESSAGE = 'Jenkins: Auto-commit after build'
    }

    // Auto-trigger every 5 mins on Git changes
    triggers {
        pollSCM('H H * * *')
    }

    stages {
        stage('Checkout from Git') {
            steps {
                git branch: "${env.BRANCH_NAME}",
                    url: 'https://github.com/Vigneshwar-Raipally/Saucedemo.git'
            }
        }

        stage('Copy Files from Eclipse Workspace') {
            steps {
                bat """
                echo Copying files from Eclipse workspace...
                xcopy /E /Y /I "${ECLIPSE_WORKSPACE}\\*" "."
                """
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml'
            }
        }

        stage('Commit & Push Changes') {
            steps {
                script {
                    echo 'Checking for changes to push...'
                    withCredentials([usernamePassword(
                        credentialsId: 'github-jenkins',
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_TOKEN')]) {

                        bat """
                            git config user.email "jenkins@pipeline.com"
                            git config user.name "Jenkins CI"

                            git status
                            git add .

                            REM Commit only if there are changes
                            git diff --cached --quiet || git commit -m "${COMMIT_MESSAGE}"

                            REM Push using token
                            git push https://%GIT_USER%:%GIT_TOKEN%@github.com/Vigneshwar-Raipally/Saucedemo.git ${BRANCH_NAME}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Archive screenshots
            archiveArtifacts artifacts: 'reports/screenshots/*', fingerprint: true

            // Publish Cucumber Report
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports/cucumber-reports',
                reportFiles: 'cucumber-report.html',
                reportName: 'Cucumber Report'
            ])

            // Publish Extent Report
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports/extent-reports',
                reportFiles: 'index.html',
                reportName: 'Extent Report'
            ])
        }
    }
}