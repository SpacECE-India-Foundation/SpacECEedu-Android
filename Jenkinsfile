pipeline {
    agent any

    environment {
        BUILD_ID = "${BUILD_NUMBER}" // Auto-incremented build number
    }

    stages {
        stage('Clone Repository') {
            steps {
                git 'https://github.com/SpacECE-India-Foundation/SpacECEedu-Android.git'
            }
        }

        stage('Update Build ID in About Section') {
            steps {
                script {
                    sh '''
                    sed -i "s/BUILD_ID:.*/BUILD_ID: ${BUILD_ID}/g" /var/www/html/SpacECEedu-Android/app/src/main/res/values/ids.xml
                    git add /var/www/html/SpacECEedu-Android/app/src/main/res/values/ids.xml
                    git commit -m "Auto-update build ID to ${BUILD_ID}"
                    '''
                }
            }
        }

        stage('Build APK') {
            steps {
                sh './gradlew assembleDebug'
            }
        }

        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk', fingerprint: true
            }
        }

        stage('Deliver APK') {
            steps {
                script {
                    def apkPath = "app/build/outputs/apk/debug/app-debug.apk"
                    sh "curl -X POST -F 'file=@${apkPath}' http://3.109.206.67/SpacECEedu-Android/"
                }
            }
        }
    }

    post {
        success {
            echo "Build and deployment successful!"
        }
        failure {
            echo "Build failed!"
        }
    }
}
