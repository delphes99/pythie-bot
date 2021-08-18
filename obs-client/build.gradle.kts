plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    implementation("io.ktor:ktor-client:1.6.0")
    implementation("io.ktor:ktor-client-cio-jvm:1.6.0")
    implementation("io.ktor:ktor-client-serialization-jvm:1.6.0")
    implementation("io.ktor:ktor-client-logging-jvm:1.6.0")
}

description = "obs-client"
