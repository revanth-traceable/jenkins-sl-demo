#!/usr/bin/env groovy

def call(Map config = [:]) {
    String pipelineType = (config.pipelineType ?: 'var1').toString().trim().toLowerCase()

    // Support both styles:
    // 1) { pipelineType: var1, pipelineConfig: { ... } }
    // 2) { pipelineType: var1, build: { ... }, deploy: { ... } }
    Map pipelineConfig = config.pipelineConfig instanceof Map
        ? config.pipelineConfig
        : config.findAll { key, _ -> key != 'pipelineType' }

    echo "ciPipeline selected pipelineType='${pipelineType}'"

    switch (pipelineType) {
        case 'var1':
            return var1(pipelineConfig)
        case 'var2':
            return var2(pipelineConfig)
        default:
            error("Unsupported pipelineType '${pipelineType}'. Supported values: var1, var2")
    }
}
