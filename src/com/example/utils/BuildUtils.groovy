package com.example.utils

class BuildUtils implements Serializable {
    
    // Calls StringUtils methods from another file
    static String generateBuildMessage(String buildNumber, String status) {
        String upperStatus = StringUtils.toUpperCase(status)
        return StringUtils.formatString("Build #${buildNumber}: ${upperStatus}", "*** ", " ***")
    }
    
    static String createDeploymentMessage(String environment, String version) {
        String envUpper = StringUtils.toUpperCase(environment)
        return StringUtils.formatString("Deploying v${version} to ${envUpper}", ">>> ", " <<<")
    }
}
