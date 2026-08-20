package no.nav.tsm.core

import io.ktor.server.config.*
import kotlin.time.Duration
import no.nav.tsm.ktor.nais.RuntimeCluster
import no.nav.tsm.ktor.nais.getRuntimeCluster

class Runtime(val env: RuntimeCluster, val name: String)

class Environment(val runtime: Runtime, val sykmeldingerConsumer: SykmeldingerConsumer)

class SykmeldingerConsumer(val pollInterval: Duration)

fun initializeEnvironment(config: ApplicationConfig): Environment {

    return Environment(
        runtime =
            Runtime(env = getRuntimeCluster(), name = config.property("app.name").getString()),
        sykmeldingerConsumer =
            SykmeldingerConsumer(
                pollInterval = config.property("sykmeldingConsumer.longPoll").getAs()
            ),
    )
}
