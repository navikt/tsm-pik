package no.nav.tsm.modules.etterlevelse.service

import no.nav.tsm.ktor.kafka.producer.KafkaRecordProducer
import no.nav.tsm.ktor.logger
import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingKafkaMessage

class EtterlevelseProducerService(
    private val juridiskVurderingProducer: KafkaRecordProducer<JuridiskVurderingKafkaMessage>
) {
    private val logger = logger()

    fun sendToKafka(juridiskVurderingKafkaMessage: JuridiskVurderingKafkaMessage) {
        try {
            juridiskVurderingProducer.send(
                juridiskVurderingKafkaMessage.fodselsnummer,
                juridiskVurderingKafkaMessage,
            )
        } catch (ex: Exception) {
            logger.error(
                "Failed to send message to kafka for id ${juridiskVurderingKafkaMessage.id}, sporing ${juridiskVurderingKafkaMessage.sporing}"
            )
            throw ex
        }
    }
}
