package no.nav.tsm.modules.pik.service

import no.nav.tsm.ktor.logger
import no.nav.tsm.modules.etterlevelse.mapper.tilJuridiskVurderingKafkaMessage
import no.nav.tsm.modules.etterlevelse.service.EtterlevelseProducerService
import no.nav.tsm.modules.pik.model.JuridiskHenvisningRecord

class PikConsumerService(val etterlevelseProducerService: EtterlevelseProducerService) {
    private val logger = logger()

    fun handleRecord(record: JuridiskHenvisningRecord) {
        logger.info("Processing JuridiskHenvisningRecord, contains ${record.juridiskeVurderinger.size} of juridiskVurderinger")

        record.juridiskeVurderinger.forEach { juridiskVurdering ->
            etterlevelseProducerService.sendToKafka(
                juridiskVurdering.tilJuridiskVurderingKafkaMessage()
            )
        }
    }
}
