pipeline {
    agent any

    environment {
        REMOTE_SERVER = 'android-server-space'  // SSH server configuration name in Jenkins
        REMOTE_DIR = '/var/www/html/apk/'        // Directory on the remote server to store APK
        GIT_REPO_URL = 'https://github.com/SpacECE-India-Foundation/SpacECEedu-Android.git'  // Git repository URL
        BRANCH_NAME = 'SpacECEQ324-NewUI'        // Branch name to build from
    }

    stages {
        stage('Clone Repository') {
            steps {
                // Clone the Android app code from the specified Git repository and branch
                git branch: BRANCH_NAME, url: GIT_REPO_URL
            }
        }

        stage('Set Permissions for gradlew') {
            steps {
                // Add execute permission to gradlew
                sh 'chmod +x gradlew'
            }
        }

        stage('Build APK') {
            steps {
                // Run Gradle build to generate APK (for debug build in this example)
                sh './gradlew assembleDebug'
            }
        }

        stage('Archive APK') {
            steps {
                // Archive the APK to make it accessible as a Jenkins artifact
                archiveArtifacts artifacts: '**/app/build/outputs/apk/debug/*.apk', fingerprint: true
            }
        }

        stage('Deploy to Remote Server') {
            steps {
                // Transfer the APK to the remote server
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: REMOTE_SERVER,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: '**/app/build/outputs/apk/debug/*.apk',  // Path to the APK file in Jenkins workspace
                                    remoteDirectory: REMOTE_DIR,  // Remote directory where APK will be uploaded
                                    removePrefix: '',  // Optional: Remove any prefix from source path
                                    flatten: true       // Optional: Upload files directly without folder structure
                                )
                            ],
                            usePromotionTimestamp: false,
                            useWorkspaceInPromotion: false,
                            verbose: true
                        )
                    ]
                )
            }
        }

        stage('Notify QA') {
            steps {
                // Notify QA team about the APK availability
                echo "APK is available at: http://65.0.243.45/apk/"
            }
        }
    }
}
