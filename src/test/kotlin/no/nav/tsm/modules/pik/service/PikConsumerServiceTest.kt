package no.nav.tsm.modules.pik.service

import io.kotest.matchers.equals.shouldEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import no.nav.tsm.modules.etterlevelse.service.EtterlevelseProducerService
import no.nav.tsm.modules.pik.model.JuridiskHenvisningRecord
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisning
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisningLovverk
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering

class PikConsumerServiceTest {
    private val etterlevelseProducerService = mockk<EtterlevelseProducerService>()
    private val record: JuridiskHenvisningRecord =
        JuridiskHenvisningRecord(
            juridiskeVurderinger =
                listOf(
                    JuridiskVurdering(
                        id = UUID.randomUUID().toString(),
                        eventName = "subsumsjon",
                        version = "1.0.0",
                        kilde = "syfosmregler",
                        versjonAvKode = "imagenavn",
                        fodselsnummer = "12345678910",
                        juridiskHenvisning =
                            JuridiskHenvisning(
                                lovverk = JuridiskHenvisningLovverk.FOLKETRYGDLOVEN,
                                paragraf = "§8-1",
                                ledd = 1,
                                punktum = 1,
                                bokstav = "a",
                            ),
                        sporing = mapOf("sykmeldingId" to UUID.randomUUID().toString()),
                        input = mapOf("input" to "verdi"),
                        utfall = JuridiskUtfall.VILKAR_OPPFYLT,
                        tidsstempel = ZonedDateTime.now(ZoneOffset.UTC),
                    ),
                    JuridiskVurdering(
                        id = UUID.randomUUID().toString(),
                        eventName = "subsumsjon",
                        version = "1.0.0",
                        kilde = "syfosmregler",
                        versjonAvKode = "imagenavn",
                        fodselsnummer = "12345678910",
                        juridiskHenvisning =
                            JuridiskHenvisning(
                                lovverk = JuridiskHenvisningLovverk.FOLKETRYGDLOVEN,
                                paragraf = "§8-1",
                                ledd = 1,
                                punktum = 1,
                                bokstav = "a",
                            ),
                        sporing = mapOf("sykmeldingId" to UUID.randomUUID().toString()),
                        input = mapOf("input" to "verdi"),
                        utfall = JuridiskUtfall.VILKAR_OPPFYLT,
                        tidsstempel = ZonedDateTime.now(ZoneOffset.UTC),
                    ),
                )
        )

    @Test
    fun `Should send to send kakfa topic`() {
        val pikConsumerService = PikConsumerService(etterlevelseProducerService)
        coEvery { etterlevelseProducerService.sendToKafka(any()) } returns Unit

        val handleRecordResult = runCatching { pikConsumerService.handleRecord(record) }

        handleRecordResult.isSuccess shouldEqual true
        coVerify(exactly = 2) { etterlevelseProducerService.sendToKafka(any()) }
    }
}
