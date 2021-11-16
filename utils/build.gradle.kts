plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    testImplementation(libs.bundles.kotlin.test)
}

description = "fr.delphes.utils"
