pipeline {
    agent any

    tools {
        maven "maven-Default"
        jdk "Java-21"
    }

    stages {
        stage('Git fetch') {
            steps {
                git branch: 'main', url: 'https://github.com/dcf313/mavenEjercicios'
            }
        }

        stage('Build & Test') {
            steps {
                // Generamos el paquete y el binario de cobertura (jacoco.exec)
                sh "mvn -f pom.xml clean package"
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts '**/target/*.jar'
                    
                    // Publica la gráfica de cobertura básica desde el .exec
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/test/'
                    )
                }
            }
        }

        stage('Reports & Documentation') {
            steps {
                // Usamos 'site' para generar TODA la documentación y reportes 
                // (incluye Javadoc, Checkstyle, PMD y JaCoCo en HTML/XML)
                // Saltamos dependency-check para evitar el error 401 que vimos antes
                sh "mvn -f pom.xml site -Ddependency-check.skip=true"
            }
            post {
                success {
                    // 1. Archivar Javadoc (Ruta estándar de Maven Site)
                    javadoc javadocDir: 'target/site/apidocs', keepAll: false
                    
                    // 2. Publicar el sitio completo de Maven
                    publishHTML(target: [
                        reportName: 'Maven Site', 
                        reportDir: 'target/site', 
                        reportFiles: 'index.html', 
                        keepAll: false
                    ])

                    // 3. Adaptador de Cobertura (necesita el XML generado por site)
                    publishCoverage adapters: [jacocoAdapter('**/target/site/jacoco/jacoco.xml')]

                    // 4. Análisis estático de código
                    recordIssues enabledForFailure: true, tool: checkStyle()
                    recordIssues enabledForFailure: true, tool: pmdParser()
                    recordIssues enabledForFailure: true, tool: spotBugs()
                }
            }
        }
    }
}
