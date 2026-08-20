package no.nav.tsm

import io.ktor.server.application.*
import no.nav.tsm.modules.etterlevelse.configureEtterlevelse
import no.nav.tsm.modules.pik.configurePik
import no.nav.tsm.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Global configuration
    configureDependencies()
    configureMonitoring()

    // Specific modules
    configurePik()
    configureEtterlevelse()
}
