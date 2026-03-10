def sharedLibrary = [
    name: 'jenkins-demo-library-trusted',
    version: 'release/1.0.2'
]

library identifier: "${sharedLibrary.name}@${sharedLibrary.version}", changelog: false
def ciConfig
def configFile = env.CI_CONFIG_FILE ?: 'ci-config.yaml'

node {
    stage('Checkout') {
        checkout scm
    }

    stage('Load Config') {
        if (!fileExists(configFile)) {
            error("Config file not found: ${configFile}")
        }

        ciConfig = readYaml file: configFile
        if (!(ciConfig instanceof Map)) {
            error("${configFile} must contain a YAML object")
        }
        echo "Loaded pipelineType: ${ciConfig.pipelineType ?: 'var1'} from ${configFile}"
    }

    ciPipeline(ciConfig)
}
