package no.nav.tsm.modules.etterlevelse.service

import io.mockk.coVerify
import io.mockk.mockk
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import no.nav.tsm.ktor.kafka.producer.KafkaRecordProducer
import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.tsm.modules.etterlevelse.model.Utfall

class EtterlevelseProducerServiceTest {
    val juridiskVurderingProducer =
        mockk<KafkaRecordProducer<JuridiskVurderingKafkaMessage>>(relaxed = true)
    private val etterlevelseProducerService =
        EtterlevelseProducerService(juridiskVurderingProducer = juridiskVurderingProducer)

    @Test
    fun `Should send juridiskVurderingKafkaMessage to kafka topic`() {
        val juridiskVurderingKafkaMessage =
            JuridiskVurderingKafkaMessage(
                id = UUID.randomUUID(),
                tidsstempel = OffsetDateTime.now(),
                eventName = "subsumsjon",
                versjon = "1.0.0",
                kilde = "syfosmregler",
                versjonAvKode = "imagenavn",
                fodselsnummer = "12345678910",
                sporing = mapOf("sykmeldingId" to listOf(UUID.randomUUID().toString())),
                lovverk = "folketrygdloven",
                lovverksversjon = "2022-01-01",
                paragraf = "§8-1",
                ledd = 1,
                punktum = 1,
                bokstav = "a",
                input = mapOf("input" to "verdi"),
                output = null,
                utfall = Utfall.VILKAR_OPPFYLT,
            )

        etterlevelseProducerService.sendToKafka(juridiskVurderingKafkaMessage)

        coVerify(exactly = 1) {
            juridiskVurderingProducer.send(
                juridiskVurderingKafkaMessage.fodselsnummer,
                juridiskVurderingKafkaMessage,
            )
        }
    }
}
