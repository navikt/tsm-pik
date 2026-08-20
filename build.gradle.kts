import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import dev.detekt.gradle.Detekt

group = "no.nav.tsm"
version = "0.0.1"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    // Ktor
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)

    // Kafa
    implementation(libs.kafka.client)


    // TSM libraries
    implementation(tsmKtorLibs.core)
    implementation(tsmKtorLibs.kafka)
    implementation(libs.tsm.regula)


    // Monitoring and logging
    implementation(libs.logback.classic)
    implementation(libs.logback.encoder)
    constraints {
        implementation(libs.jackson.core) {
            because("Vulnerability CVE-2026-29062 from libs.logback.encoder")
        }
    }

    // Test
    testImplementation(libs.kotlin.test.junit)
    testImplementation(ktorLibs.server.testHost)
    testImplementation(ktorLibs.serialization.jackson3)
    testImplementation(ktorLibs.client.contentNegotiation)

}

kotlin {
    jvmToolchain(libs.versions.jvmVersion.get().toInt())
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        mergeServiceFiles {}
        from("src/main/resources/logback.xml") {
            into("/")
        }
    }

    configure<SpotlessExtension> {
        kotlin { ktfmt(libs.versions.ktfmt.get()).kotlinlangStyle() }
        check {
            dependsOn("spotlessApply")
        }
    }

    register<JavaExec>("runLocal") {
        description = "Running application locally"
        group = "application"
        mainClass.set("io.ktor.server.netty.EngineMain")
        classpath = sourceSets["main"].runtimeClasspath

        args("-config=application-local.conf")
        jvmArgs("-Dio.ktor.development=true", "-Dlogback.configurationFile=logback-local.xml")


    }

    withType<Detekt>().configureEach {
        config.setFrom(file("detekt.yml"))
        buildUponDefaultConfig = true

        dependsOn("spotlessApply")
    }

    named<DependencyUpdatesTask>("dependencyUpdates") {
        fun String.isNonStable(): Boolean {
            val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeyword || regex.matches(this)
            return isStable.not()
        }

        rejectVersionIf {
            candidate.version.isNonStable()
        }
    }

}

afterEvaluate {
    tasks.named("check") {
        setDependsOn(dependsOn.filter { !it.toString().contains("detekt") })
    }
}
