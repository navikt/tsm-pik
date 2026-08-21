rootProject.name = "tsm-pik"


val ktorVersion = "3.5.2"
val tsmKtorVersion = "1.2.10"

dependencyResolutionManagement {
    repositories {
        maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release") {
            content {
                includeGroup("no.nav.tsm")
            }
        }
        maven("https://jitpack.io") {
            content {
                includeGroup("dev.hayden")
            }
        }
        mavenCentral {
            content {
                excludeGroup("no.nav.tsm")
                excludeGroup("dev.hayden")
            }
        }

    }

    versionCatalogs {
        create("ktorLibs").from("io.ktor:ktor-version-catalog:${ktorVersion}")
        create("tsmKtorLibs").from("no.nav.tsm:ktor-version-catalog:${tsmKtorVersion}")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("io.github.ben-manes.versions.settings") version "0.61.0"
}
