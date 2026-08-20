package no.nav.tsm.modules.pik

import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import kotlin.time.Duration.Companion.seconds
import no.nav.tsm.core.Environment
import no.nav.tsm.ktor.kafka.consumer.KafkaConsumer
import no.nav.tsm.ktor.logger
import no.nav.tsm.modules.pik.model.JuridiskHenvisningRecord
import no.nav.tsm.modules.pik.service.PikConsumerService

fun Application.configurePik() {
    configureDependencies()
    configureConsumer()
}

private fun Application.configureConsumer() {
    val log = logger()
    val env: Environment by dependencies
    val pikConsumerService: PikConsumerService by dependencies

    install(KafkaConsumer) {
        clientId = env.runtime.name
        groupId = "tsm-pik-consumer"
        pollDuration = 10.seconds
        retryDuration = 60.seconds

        consume<JuridiskHenvisningRecord>(
            name = "tsm.pik",
            onTombstone = { meta ->
                log.info("Mottok en JuridiskHenvisning tombstone for ID ${meta.key}, hopper over")
            },
            onRecord = { record -> pikConsumerService.handleRecord(record) },
        )
    }
}

private fun Application.configureDependencies() {
    dependencies { provide(PikConsumerService::class) }
}
