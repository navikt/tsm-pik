package no.nav.tsm.modules.etterlevelse.mapper

import io.kotest.matchers.equals.shouldEqual
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingResult
import no.nav.tsm.modules.etterlevelse.model.Utfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisning
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisningLovverk
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering

class JuridiskVurderingMapperTest {

    @Test
    fun `validate juridiskVurderingKafkaMessage mapps correctly`() {
        val id = UUID.randomUUID()
        val sykmeldingId = UUID.randomUUID().toString()
        val juridiskVurderingResult =
            JuridiskVurderingResult(
                listOf(
                    JuridiskVurdering(
                        id = id.toString(),
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
                        sporing = mapOf("sykmeldingId" to sykmeldingId),
                        input = mapOf("input" to "verdi"),
                        utfall = JuridiskUtfall.VILKAR_OPPFYLT,
                        tidsstempel = ZonedDateTime.now(ZoneOffset.UTC),
                    )
                )
            )

        val tidsstempel = OffsetDateTime.now()
        val juridiskVurderingKafkaMessage =
            juridiskVurderingResult.juridiskeVurderinger
                .first()
                .tilJuridiskVurderingKafkaMessage(tidsstempel)

        juridiskVurderingKafkaMessage.id shouldEqual id
        juridiskVurderingKafkaMessage.tidsstempel shouldEqual tidsstempel
        juridiskVurderingKafkaMessage.eventName shouldEqual "subsumsjon"
        juridiskVurderingKafkaMessage.versjon shouldEqual "1.0.0"
        juridiskVurderingKafkaMessage.kilde shouldEqual "syfosmregler"
        juridiskVurderingKafkaMessage.versjonAvKode shouldEqual "imagenavn"
        juridiskVurderingKafkaMessage.fodselsnummer shouldEqual "12345678910"
        juridiskVurderingKafkaMessage.sporing shouldEqual
            mapOf("sykmeldingId" to listOf(sykmeldingId))
        juridiskVurderingKafkaMessage.lovverk shouldEqual "folketrygdloven"
        juridiskVurderingKafkaMessage.lovverksversjon shouldEqual "2022-01-01"
        juridiskVurderingKafkaMessage.paragraf shouldEqual "§8-1"
        juridiskVurderingKafkaMessage.ledd shouldEqual 1
        juridiskVurderingKafkaMessage.punktum shouldEqual 1
        juridiskVurderingKafkaMessage.bokstav shouldEqual "a"
        juridiskVurderingKafkaMessage.input shouldEqual mapOf("input" to "verdi")
        juridiskVurderingKafkaMessage.output shouldEqual null
        juridiskVurderingKafkaMessage.utfall shouldEqual Utfall.VILKAR_OPPFYLT
    }
}
