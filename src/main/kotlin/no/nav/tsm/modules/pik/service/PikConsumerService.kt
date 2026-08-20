package no.nav.tsm.modules.pik.service

import no.nav.tsm.modules.etterlevelse.mapper.tilJuridiskVurderingKafkaMessage
import no.nav.tsm.modules.etterlevelse.service.EtterlevelseProducerService
import no.nav.tsm.modules.pik.model.JuridiskHenvisningRecord

class PikConsumerService(val etterlevelseProducerService: EtterlevelseProducerService) {

    fun handleRecord(record: JuridiskHenvisningRecord) {
        record.juridiskeVurderinger.forEach { juridiskVurdering ->
            etterlevelseProducerService.sendToKafka(
                juridiskVurdering.tilJuridiskVurderingKafkaMessage()
            )
        }
    }
}
