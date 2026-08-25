package no.nav.tsm.core

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import kotlin.collections.plus
import kotlin.test.Test
import kotlin.test.assertEquals

class EnvironmentTest {

    private fun config(overrides: Map<String, String>) =
        HoconApplicationConfig(
            ConfigFactory.parseMap(baseNaisVars + overrides)
                .withFallback(ConfigFactory.parseResources("application.conf"))
                .resolve()
        )

    private val baseNaisVars =
        mapOf("NAIS_POD_NAME" to "tsm-pik-prod-123", "NAIS_CLUSTER_NAME" to "prod-gcp")

    @Test
    fun `production environment should be properly configured`() {

        val environment = initializeEnvironment(config(emptyMap()))

        assertEquals("tsm-pik-prod-123", environment.runtime.name)
        assertEquals("LOCAL", environment.runtime.env.name)
    }
}
