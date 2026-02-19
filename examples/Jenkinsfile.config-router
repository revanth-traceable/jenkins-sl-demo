def sharedLibrary = [
    name: 'jenkins-demo-library-trusted',
    version: 'release/1.0.2'
]

// Load shared library without @Library annotation.
// Keep "release/1.0.2" as the default version in Jenkins global library config.
library identifier: sharedLibrary.name, changelog: false

def ciConfig
def configFile = env.CI_CONFIG_FILE ?: 'ci-config.yaml'

node {
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
