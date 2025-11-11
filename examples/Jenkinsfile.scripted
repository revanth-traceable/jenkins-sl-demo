@Library('jenkins-demo-library') _

import com.example.utils.StringUtils
import com.example.utils.BuildUtils
import com.example.notification.NotificationService

node {
    try {
        def notificationService = new NotificationService(this)
        
        stage('Initialize') {
            // Direct use of StringUtils class
            def projectName = StringUtils.formatString("Demo App", "=== ", " ===")
            echo projectName
            
            // Direct use of BuildUtils class (which calls StringUtils)
            def buildMsg = BuildUtils.generateBuildMessage(env.BUILD_NUMBER, "INITIALIZED")
            echo buildMsg
        }
        
        stage('Build') {
            def buildResult = buildApp()
            echo "Build completed: ${buildResult}"
        }
        
        stage('Deploy') {
            def deployResult = deployApp([
                environment: 'dev',
                version: "1.0.${env.BUILD_NUMBER}"
            ])
            echo "Deployment completed: ${deployResult}"
        }
        
        echo "Pipeline completed!"
        
    } catch (Exception e) {
        echo "Pipeline failed: ${e.message}"
        throw e
    }
}
