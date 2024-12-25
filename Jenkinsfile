pipeline {
    agent any

    stages {
        stage('Deploy Android Application') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'android-server',
                        transfers: [
                            sshTransfer(
                                cleanRemote: false,
                                excludes: '',
                                execCommand: '',
                                execTimeout: 120000,
                                flatten: false,
                                makeEmptyDirs: false,
                                noDefaultExcludes: false,
                                patternSeparator: '[, ]+',
                                remoteDirectory: '/var/www/html',
                                remoteDirectorySDF: false,
                                removePrefix: '',
                                sourceFiles: '**/*.jar'
                            )
                        ],
                        usePromotionTimestamp: false,
                        useWorkspaceInPromotion: false,
                        verbose: false
                    )
                ])
            }
        }
    }
}
