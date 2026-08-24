package no.nav.tsm.modules.etterlevelse.mapper

import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingResult
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisning
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisningLovverk
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test

class JuridiskVurderingSchemaTest {

    @Test
    fun `check that juridiskVurderingSchema is valid`() {

        val objectMapper =
            JsonMapper.builder()
                .addModule(kotlinModule())
                .configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build()

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
                                paragraf = "8-1",
                                ledd = 1,
                                punktum = 1,
                                bokstav = "a"
                            ),
                        sporing = mapOf("sykmelding" to sykmeldingId),
                        input = mapOf("input" to "verdi"),
                        utfall = JuridiskUtfall.VILKAR_OPPFYLT,
                        tidsstempel = ZonedDateTime.now(ZoneOffset.UTC)
                    )
                )
            )

        val tidsstempel = OffsetDateTime.now()
        val juridiskVurderingKafkaMessage =
            juridiskVurderingResult.juridiskeVurderinger
                .first()
                .tilJuridiskVurderingKafkaMessage(tidsstempel)
        val kafkaMessage = objectMapper.writeValueAsString(juridiskVurderingKafkaMessage)

        SchemaAssertions.assertSchema(kafkaMessage)
    }
}