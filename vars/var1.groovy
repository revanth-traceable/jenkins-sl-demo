#!/usr/bin/env groovy

def call(Map config = [:]) {
    echo "Running pipeline variant: var1 (build-only)"
    Map buildConfig = config.build instanceof Map ? config.build : [:]
    return buildApp(buildConfig)
}
