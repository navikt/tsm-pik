package no.nav.tsm.modules.etterlevelse.mapper

import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingResult
import no.nav.tsm.modules.etterlevelse.model.Utfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisning
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisningLovverk
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering
import kotlin.test.Test
import kotlin.test.assertEquals

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

        assertEquals(id, juridiskVurderingKafkaMessage.id)
        assertEquals(tidsstempel, juridiskVurderingKafkaMessage.tidsstempel)
        assertEquals("subsumsjon", juridiskVurderingKafkaMessage.eventName)
        assertEquals("1.0.0", juridiskVurderingKafkaMessage.versjon)
        assertEquals("syfosmregler", juridiskVurderingKafkaMessage.kilde)
        assertEquals("imagenavn", juridiskVurderingKafkaMessage.versjonAvKode)
        assertEquals("12345678910", juridiskVurderingKafkaMessage.fodselsnummer)
        assertEquals(mapOf("sykmeldingId" to listOf(sykmeldingId)), juridiskVurderingKafkaMessage.sporing)
        assertEquals("folketrygdloven", juridiskVurderingKafkaMessage.lovverk)
        assertEquals("2022-01-01", juridiskVurderingKafkaMessage.lovverksversjon)
        assertEquals("§8-1", juridiskVurderingKafkaMessage.paragraf)
        assertEquals(1, juridiskVurderingKafkaMessage.ledd)
        assertEquals(1, juridiskVurderingKafkaMessage.punktum)
        assertEquals("a", juridiskVurderingKafkaMessage.bokstav)
        assertEquals(mapOf("input" to "verdi"), juridiskVurderingKafkaMessage.input)
        assertEquals(null, juridiskVurderingKafkaMessage.output)
        assertEquals(Utfall.VILKAR_OPPFYLT, juridiskVurderingKafkaMessage.utfall)

    }
}
