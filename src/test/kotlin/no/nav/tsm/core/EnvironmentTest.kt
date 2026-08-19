package no.nav.tsm.core

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import kotlin.test.Test
import kotlin.test.assertEquals

class EnvironmentTest {
    @Test
    fun `production environment should be properly configured`() {
        val applicationConfig =
            HoconApplicationConfig(
                ConfigFactory.parseMap(
                        mapOf(
                            "NAIS_POD_NAME" to "tsm-pik-prod-123",
                            "NAIS_CLUSTER_NAME" to "prod-gcp",
                        )
                    )
                    .withFallback(ConfigFactory.parseResources("application.conf"))
                    .resolve()
            )

        val environment = initializeEnvironment(applicationConfig)

        assertEquals("tsm-pik-prod-123", environment.runtime.name)
        assertEquals("LOCAL", environment.runtime.env.name)
    }
}
