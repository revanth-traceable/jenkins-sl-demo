#!/usr/bin/env groovy
import com.example.notification.NotificationService
import com.example.utils.BuildUtils

def call(Map config = [:]) {
    def buildNumber = env.BUILD_NUMBER ?: "0"
    def notificationService = new NotificationService(this)
    
    try {
        notificationService.sendBuildNotification(buildNumber, "STARTED")
        
        stage('Compile') {
            echo "Compiling application..."
            // Native Jenkins sh function
            sh 'echo "Running compilation..." && sleep 1'
            echo "Compilation complete"
        }
        
        stage('Test') {
            echo "Running tests..."
            // Native Jenkins sh function with script
            sh '''
                echo "Running unit tests..."
                echo "Test suite: PASSED"
            '''
        }
        
        stage('Package') {
            // Use BuildUtils (cross-file call)
            def message = BuildUtils.generateBuildMessage(buildNumber, "PACKAGING")
            echo message
            
            // Native writeFile and readFile functions
            writeFile file: 'build-info.txt', text: "Build: ${buildNumber}\nStatus: SUCCESS"
            def buildInfo = readFile 'build-info.txt'
            echo "Build info:\n${buildInfo}"
        }
        
        notificationService.sendBuildNotification(buildNumber, "SUCCESS")
        return [success: true, buildNumber: buildNumber]
        
    } catch (Exception e) {
        notificationService.sendBuildNotification(buildNumber, "FAILED")
        throw e
    }
}
