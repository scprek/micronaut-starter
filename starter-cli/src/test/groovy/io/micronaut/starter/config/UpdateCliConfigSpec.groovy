package io.micronaut.starter.config

import io.micronaut.context.BeanContext
import io.micronaut.starter.CommandFixture
import io.micronaut.starter.CommandSpec
import io.micronaut.starter.ConsoleOutput
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.config.UpdateCliConfig
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup

class UpdateCliConfigSpec extends CommandSpec implements CommandFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "test old cli config conversion - gradle"() {
        generateDefaultProject(Language.JAVA)
        File cliYaml = new File(dir, "micronaut-cli.yml").getCanonicalFile()
        //Replace the config with the old style
        cliYaml.write("""profile: service
defaultPackage: micronaut.testing.keycloak
---
testFramework: junit
sourceLanguage: java""")
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)

        when:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, consoleOutput)

        then:
        2 * consoleOutput.warning(_)
        codeGenConfig.buildTool == BuildTool.GRADLE
        codeGenConfig.sourceLanguage == Language.JAVA
        codeGenConfig.testFramework == TestFramework.JUNIT
        codeGenConfig.applicationType == ApplicationType.DEFAULT
        codeGenConfig.defaultPackage == "micronaut.testing.keycloak"
        codeGenConfig.legacy

        when:
        Integer exitCode = new UpdateCliConfig(codeGenConfig, () -> new FileSystemOutputHandler(dir, consoleOutput)) {
            @Override
            void out(String message) {
                consoleOutput.out(message)
            }
        }.call()

        then:
        noExceptionThrown()
        exitCode == 0
        cliYaml.text.matches("""applicationType: default
defaultPackage: micronaut.testing.keycloak
testFramework: junit
sourceLanguage: java
buildTool: gradle
features: \\[.*?\\]
""")
        2 * consoleOutput.out(_)
        1 * consoleOutput.out("For a list of available features, run `mn create-app --list-features`")

    }

    void "test old cli config conversion - maven"() {
        generateDefaultProject(Language.JAVA, BuildTool.MAVEN)
        File cliYaml = new File(dir, "micronaut-cli.yml").getCanonicalFile()
        //Replace the config with the old style
        cliYaml.write("""profile: service
defaultPackage: micronaut.testing.keycloak
---
testFramework: junit
sourceLanguage: java""")
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)

        when:
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, consoleOutput)

        then:
        2 * consoleOutput.warning(_)
        codeGenConfig.buildTool == BuildTool.MAVEN
        codeGenConfig.sourceLanguage == Language.JAVA
        codeGenConfig.testFramework == TestFramework.JUNIT
        codeGenConfig.applicationType == ApplicationType.DEFAULT
        codeGenConfig.defaultPackage == "micronaut.testing.keycloak"
        codeGenConfig.legacy

        when:
        Integer exitCode = new UpdateCliConfig(codeGenConfig, () -> new FileSystemOutputHandler(dir, consoleOutput)) {
            @Override
            void out(String message) {
                consoleOutput.out(message)
            }
        }.call()

        then:
        noExceptionThrown()
        exitCode == 0
        cliYaml.text.matches("""applicationType: default
defaultPackage: micronaut.testing.keycloak
testFramework: junit
sourceLanguage: java
buildTool: maven
features: \\[.*?\\]
""")
        2 * consoleOutput.out(_)
        1 * consoleOutput.out("For a list of available features, run `mn create-app --list-features`")

    }
}
