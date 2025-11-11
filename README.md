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
│   └── deployApp.groovy                # Deployment function
└── examples/
    ├── Jenkinsfile.simple             # Basic declarative pipeline
    ├── Jenkinsfile.full               # Build + deploy pipeline
    └── Jenkinsfile.scripted           # Scripted pipeline
```

## Key Features

- **Cross-file function calls**: BuildUtils calls StringUtils methods
- **Multi-level calls**: NotificationService → BuildUtils → StringUtils
- **Native Jenkins functions**: Uses `sh`, `writeFile`, `readFile`, `dir`
- **Multiple pipeline styles**: Declarative and scripted examples

## Setup in Jenkins

1. Go to **Manage Jenkins** → **Configure System**
2. Under **Global Pipeline Libraries**, click **Add**
3. Configure:
   - **Name**: `jenkins-demo-library`
   - **Default version**: `main` (or your branch)
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

## Function Call Chain

```
Jenkinsfile
    └─> buildApp() or deployApp()
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
