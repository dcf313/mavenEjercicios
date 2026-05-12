pipeline {
    agent any

    tools {
        // Configuraciones confirmadas para tu instancia de Jenkins
        maven "maven-Default"
        jdk "Java-21"
    }

    stages {
        stage('Git fetch') {
            steps {
                git branch: 'main', url: 'https://github.com/ualhmis/MavenEjercicios'
            }
        }

        stage('Compile, Test, Package') {
            steps {
                // Ejecuta el ciclo de vida completo hasta package
                sh "mvn -f sesion07Maven/pom.xml clean package"
            }
            post {
                success {
                    // Publica resultados de tests unitarios
                    junit '**/target/surefire-reports/TEST-*.xml'
                    
                    // Guarda el archivo JAR generado
                    archiveArtifacts '**/target/*.jar'
                    
                    // Genera y publica el informe de cobertura JaCoCo
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/test/'
                    )
                    
                    // Adaptador para el plugin de "Coverage" (tendencias modernas)
                    publishCoverage adapters: [jacocoAdapter('**/target/site/jacoco/jacoco.xml')]
                }
            }
        }
        
            stage ('Documentation') {
      steps {
	    sh "mvn -f sesion07Maven/pom.xml javadoc:javadoc javadoc:aggregate"
      }
      post{
        success {
          step $class: 'JavadocArchiver', javadocDir: 'sesion07Maven/target/site/apidocs', keepAll: false
          publishHTML(target: [reportName: 'Maven Site', reportDir: 'sesion07Maven/target/site', reportFiles: 'index.html', keepAll: false])
        }
      }
    }

        stage('Analysis') {
            steps {
                // Genera reportes de Checkstyle y el sitio de Maven
                sh "mvn -f sesion07Maven/pom.xml site -Ddependency-check.skip=true"
            }
            post {
                success {
                    // Registra los problemas detectados por Checkstyle en la UI de Jenkins
                    dependencyCheckPublisher pattern: '**/target/site/dependency-check-report.xml'
                    recordIssues enabledForFailure: true, tool: checkStyle()
                    recordIssues enabledForFailure: true, tool: pmdParser()
                    recordIssues enabledForFailure: true, tool: cpd()
                    recordIssues enabledForFailure: true, tool: findBugs()
                    recordIssues enabledForFailure: true, tool: spotBugs()
                }
            }
        }
    }
}
