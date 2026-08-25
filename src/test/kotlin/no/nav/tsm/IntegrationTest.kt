package no.nav.tsm

import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration
import no.nav.tsm.ktor.kafka.test.KafkaContainer
import no.nav.tsm.ktor.kafka.test.send
import no.nav.tsm.modules.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.tsm.modules.pik.model.JuridiskHenvisningRecord
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisning
import no.nav.tsm.regulus.regula.juridisk.JuridiskHenvisningLovverk
import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import no.nav.tsm.regulus.regula.juridisk.JuridiskVurdering
import no.nav.tsm.utils.configureFullIntegrationTests
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue

const val PIK_TOPIC = "tsm.pik"
const val ETTERLEVELSE_TOPIC = "flex.omrade-helse-etterlevelse"

class IntegrationTest {
    val kafka = KafkaContainer(createTopics = listOf(ETTERLEVELSE_TOPIC, PIK_TOPIC))

    val producer: KafkaProducer<String, ByteArray> = kafka.createAnythingProducer()

    val mapper = jacksonObjectMapper()

    private fun ApplicationTestBuilder.configureEverythingTest() {
        kafka.configureKafka(this)

        configureFullIntegrationTests()
    }

    private fun consumeFromEtterlevelseTopic(): ByteArray {
        val config =
            kafka.config +
                mapOf(
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                    ConsumerConfig.GROUP_ID_CONFIG to "tsm",
                )

        val consumer = KafkaConsumer(config, StringDeserializer(), ByteArrayDeserializer())
        consumer.subscribe(listOf(ETTERLEVELSE_TOPIC))
        while (true) {
            val records = consumer.poll(100.milliseconds.toJavaDuration())
            if (!records.isEmpty) {
                return records.first().value()
            }
        }
    }

    @Test
    fun `Should consume from kafka topic, and produce message to kafka topic`() = testApplication {
        configureEverythingTest()

        val key = UUID.randomUUID().toString()

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

        startApplication()

        producer.send(topic = PIK_TOPIC, key = key, value = mapper.writeValueAsBytes(record))

        val juridiskVurderingKafkaMessage =
            mapper.readValue<JuridiskVurderingKafkaMessage>(consumeFromEtterlevelseTopic())

        assertEquals("1.0.0", juridiskVurderingKafkaMessage.versjon)
    }
}
