package io.micronaut.starter.generator.messaging

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.messaging.kafka.Kafka
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateMessagingKafkaKotlinMavenSpec extends CommandSpec {

    @Unroll
    void 'test basic create-messaging-app for #feature and #language and #buildTool'(ApplicationType applicationType,
                                                                                     String feature,
                                                                                     Language lang,
                                                                                     BuildTool buildTool) {
        given:
        generateMessagingProject(lang, buildTool, [feature])

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run')
        } else {
            executeMavenCommand("mn:run")
        }

        then:
        testOutputContains("Startup completed")

        where:
        applicationType         | feature    | lang          | buildTool
        ApplicationType.DEFAULT | Kafka.NAME | Language.KOTLIN | BuildTool.MAVEN
    }
}