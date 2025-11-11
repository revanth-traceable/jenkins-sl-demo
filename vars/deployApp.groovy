#!/usr/bin/env groovy
import com.example.notification.NotificationService
import com.example.utils.BuildUtils

def call(Map config) {
    def environment = config.environment ?: 'dev'
    def version = config.version ?: '1.0.0'
    def notificationService = new NotificationService(this)
    
    try {
        stage('Deploy') {
            echo "Deploying to ${environment}..."
            // Call NotificationService which calls BuildUtils (multi-level cross-file calls)
            notificationService.sendDeploymentNotification(environment, version)
            
            // Native Jenkins sh function
            sh """
                echo "Deploying version ${version} to ${environment}"
                echo "Running deployment script..."
                sleep 1
            """
        }
        
        stage('Verify') {
            echo "Running verification..."
            // Native Jenkins dir and sh functions
            dir('deployment') {
                sh 'echo "Deployment verified" > status.txt'
            }
        }
        
        def successMsg = BuildUtils.createDeploymentMessage(environment, version)
        echo "SUCCESS: ${successMsg}"
        
        return [success: true, environment: environment, version: version]
        
    } catch (Exception e) {
        echo "Deployment failed: ${e.message}"
        throw e
    }
}
