package no.nav.tsm.plugins

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.jackson3.jackson
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import no.nav.tsm.module
import org.junit.Test

class MonitoringTest {

    @Test
    fun `validate alive endpoint returns 200 http status code`() {
        testApplication {
            application.module()

            client = createClient { install(ContentNegotiation) { jackson {} } }

            val httpResponse = client.get("http://localhost:80/internal/health/alive")

            assertEquals(200, httpResponse.status.value)
        }
    }
}
