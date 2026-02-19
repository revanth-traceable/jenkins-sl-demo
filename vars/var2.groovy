#!/usr/bin/env groovy

def call(Map config = [:]) {
    echo "Running pipeline variant: var2 (build + deploy)"

    Map buildConfig = config.build instanceof Map ? config.build : [:]
    Map deployConfig = config.deploy instanceof Map ? config.deploy : [:]

    if (!deployConfig.environment) {
        deployConfig.environment = 'dev'
    }
    if (!deployConfig.version) {
        deployConfig.version = "1.0.${env.BUILD_NUMBER ?: '0'}"
    }

    def buildResult = buildApp(buildConfig)
    def deployResult = deployApp(deployConfig)

    return [build: buildResult, deploy: deployResult]
}
