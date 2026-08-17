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


    // TSM libraries
    implementation(tsmKtorLibs.core)

    // Monitoring and logging
    implementation(libs.logback.classic)
    implementation(libs.logback.encoder)
    constraints {
        implementation(libs.jackson.core) {
            because("Vulnerability CVE-2026-29062")
        }
    }

    // Test
    testImplementation(libs.kotlin.test.junit)

}

kotlin {
    jvmToolchain(25)
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
        kotlin { ktfmt("0.62").kotlinlangStyle() }
        check {
            dependsOn("spotlessApply")
        }
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

tasks.register<Exec>("preRunLocal") {
    group = "application"
    commandLine("./scripts/pre-dev.sh")
}

tasks.register<JavaExec>("runLocal") {
    group = "application"
    mainClass.set("io.ktor.server.netty.EngineMain")
    classpath = sourceSets["main"].runtimeClasspath

    args("-config=application-local.conf")
    jvmArgs("-Dio.ktor.development=true", "-Dlogback.configurationFile=logback-local.xml")

    dependsOn("preRunLocal")
}

tasks.withType<Detekt>().configureEach {
    config.setFrom(file("detekt.yml"))
    buildUponDefaultConfig = true

    dependsOn("spotlessApply")
}

/**
 * Disable auto running of detekt on build and stuff
 */
afterEvaluate {
    tasks.named("check") {
        setDependsOn(dependsOn.filter { !it.toString().contains("detekt") })
    }
}
