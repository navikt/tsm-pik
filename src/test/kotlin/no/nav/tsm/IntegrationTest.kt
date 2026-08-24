package no.nav.tsm

import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import no.nav.tsm.ktor.kafka.test.KafkaContainer
import no.nav.tsm.modules.pik.model.JuridiskHenvisningRecord
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisning
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisningLovverk
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering
import no.nav.tsm.utils.configureFullIntegrationTests

const val PIK_TOPIC = "tsm.pik"
const val ETTERLEVELSE_TOPIC = "flex.omrade-helse-etterlevelse"

class IntegrationTest {
    val kafka = KafkaContainer(createTopics = listOf(ETTERLEVELSE_TOPIC, PIK_TOPIC))

    private fun ApplicationTestBuilder.configureEverythingTest() {
        kafka.configureKafka(this)

        configureFullIntegrationTests()
    }

    @Test
    fun `Should consume from kafka topic, and produce message to kafka topic`() = testApplication {
        configureEverythingTest()

        /// val producer: KafkaProducer<String, JuridiskHenvisningRecord> =
        // kafka.createAnythingProducer()

        // val producer = application.createProducer<JuridiskHenvisningRecord>(PIK_TOPIC)

        val record =
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

        /// producer.send("test-key", record)

        // val consumer = application.createConsumer()

        /// coVerify(exactly = 1) {kafka.container.}

        // TODO assert that JuridiskVurderingKafkaMessage is put on etterlevelse topic

    }
}
