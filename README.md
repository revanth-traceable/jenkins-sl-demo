# Jenkins Shared Library Demo

A minimal Jenkins shared library for testing migration to Harness.

## Structure

```
jenkins-sl-demo/
├── src/
│   └── com/example/
│       ├── utils/
│       │   ├── StringUtils.groovy      # Basic string utilities
│       │   └── BuildUtils.groovy       # Calls StringUtils (cross-file)
│       └── notification/
│           └── NotificationService.groovy  # Calls BuildUtils (multi-level)
├── vars/
│   ├── buildApp.groovy                 # Build pipeline function
│   ├── deployApp.groovy                # Deployment function
│   ├── ciPipeline.groovy               # Config-based router (var1/var2)
│   ├── var1.groovy                     # Variant 1: build-only
│   └── var2.groovy                     # Variant 2: build + deploy
└── examples/
    ├── Jenkinsfile.simple              # Basic declarative pipeline
    ├── Jenkinsfile.full                # Build + deploy pipeline
    ├── Jenkinsfile.scripted            # Scripted pipeline
    ├── Jenkinsfile.config-router       # Project-repo Jenkinsfile
    └── ci-config.yaml                  # Project-repo pipeline config
```

## Key Features

- **Cross-file function calls**: BuildUtils calls StringUtils methods
- **Multi-level calls**: NotificationService → BuildUtils → StringUtils
- **Native Jenkins functions**: Uses `sh`, `writeFile`, `readFile`, `dir`
- **Multiple pipeline styles**: Declarative and scripted examples
- **Config-driven routing**: `ciPipeline()` selects `var1` or `var2`

## Setup in Jenkins

1. Go to **Manage Jenkins** → **Configure System**
2. Under **Global Pipeline Libraries**, click **Add**
3. Configure:
   - **Name**: `jenkins-demo-library-trusted`
   - **Default version**: `release/1.0.2`
   - **Retrieval method**: Modern SCM
   - **Source Code Management**: Git
   - **Project Repository**: Path to this repository

## Usage

### Simple Build

```groovy
@Library('jenkins-demo-library') _

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                script {
                    buildApp()
                }
            }
        }
    }
}
```

### Build and Deploy

```groovy
@Library('jenkins-demo-library') _

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                script {
                    buildApp()
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    deployApp([
                        environment: 'dev',
                        version: '1.0.0'
                    ])
                }
            }
        }
    }
}
```

### Direct Class Usage

```groovy
@Library('jenkins-demo-library') _
import com.example.utils.BuildUtils

node {
    stage('Build') {
        def msg = BuildUtils.generateBuildMessage(env.BUILD_NUMBER, "SUCCESS")
        echo msg
    }
}
```

### Config-Driven Variant Selection (Two-Repo Setup)

Use this when your `Jenkinsfile` + config are in one repository and this shared
library is in another.

`Jenkinsfile` in project repo:

```groovy
def sharedLibrary = [
    name: 'jenkins-demo-library-trusted',
    version: 'release/1.0.2'
]

library identifier: sharedLibrary.name, changelog: false

def ciConfig

node {
    stage('Checkout') {
        checkout scm
    }

    stage('Load Config') {
        ciConfig = readYaml file: 'ci-config.yaml'
        if (!(ciConfig instanceof Map)) {
            error("ci-config.yaml must contain a YAML object")
        }
    }

    ciPipeline(ciConfig)
}
```

This avoids `@Library(...)`. Set `release/1.0.2` as the default version in
Jenkins global library configuration for `jenkins-demo-library-trusted`.
Because this is a scripted pipeline, run `checkout scm` before reading files.

`ci-config.yaml` in project repo:

```yaml
pipelineType: var2

pipelineConfig:
  deploy:
    environment: staging
    version: "2.0.0"
```

Router behavior:

- `pipelineType: var1` -> calls `vars/var1.groovy` (build-only)
- `pipelineType: var2` -> calls `vars/var2.groovy` (build + deploy)
- `pipelineConfig` is optional; you can also put `build` / `deploy` at top level
- `pipelineConfig.build.skipTests: true` skips the test stage in `buildApp()`

## Function Call Chain

```
Jenkinsfile
    └─> ciPipeline(config)
        └─> var1(config) or var2(config)
            └─> buildApp() / deployApp()
                └─> NotificationService.sendBuildNotification()
                    └─> BuildUtils.generateBuildMessage()
                        └─> StringUtils.toUpperCase()
                        └─> StringUtils.formatString()
```

## Testing Migration to Harness

This library includes various patterns commonly found in Jenkins pipelines:
- Global variables from `vars/`
- Shared library classes from `src/`
- Cross-file dependencies
- Native Jenkins steps (`sh`, `writeFile`, `readFile`, `dir`)
- Both declarative and scripted pipeline syntax

Use the example Jenkinsfiles in the `examples/` directory to test migration scenarios.
