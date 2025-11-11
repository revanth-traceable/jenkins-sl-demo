package com.example.notification

import com.example.utils.BuildUtils

class NotificationService implements Serializable {
    
    def steps
    
    NotificationService(steps) {
        this.steps = steps
    }
    
    // Calls BuildUtils which internally calls StringUtils (multi-level cross-file calls)
    def sendBuildNotification(String buildNumber, String status) {
        String message = BuildUtils.generateBuildMessage(buildNumber, status)
        steps.echo "Notification: ${message}"
        return message
    }
    
    def sendDeploymentNotification(String environment, String version) {
        String message = BuildUtils.createDeploymentMessage(environment, version)
        steps.echo "Deployment Notification: ${message}"
        return message
    }
}
