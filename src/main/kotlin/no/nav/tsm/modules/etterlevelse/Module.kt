package no.nav.tsm.modules.etterlevelse

import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import no.nav.tsm.core.Environment
import no.nav.tsm.ktor.kafka.producer.KafkaProducer
import no.nav.tsm.ktor.kafka.producer.KafkaRecordProducer
import no.nav.tsm.ktor.kafka.producer.createProducer
import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.tsm.modules.etterlevelse.service.EtterlevelseProducerService

fun Application.configureEtterlevelse() {
    configureDependencies()
    configureProducer()
}

private fun Application.configureProducer() {
    val environment: Environment by dependencies

    install(KafkaProducer) { clientId = environment.runtime.name }
}

private fun Application.configureDependencies() {

    dependencies {
        provide<KafkaRecordProducer<JuridiskVurderingKafkaMessage>> {
            this@configureDependencies.createProducer(topic = "flex.omrade-helse-etterlevelse")
        }
        provide(EtterlevelseProducerService::class)
    }
}
