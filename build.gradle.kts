import com.diffplug.gradle.spotless.SpotlessExtension
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
        val ktfmtVersion: String = libs.versions.ktfmt.get()
        kotlin { ktfmt(ktfmtVersion).kotlinlangStyle() }
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

}

afterEvaluate {
    tasks.named("check") {
        setDependsOn(dependsOn.filter { !it.toString().contains("detekt") })
    }
}
