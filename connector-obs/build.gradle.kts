plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    implementation(project(":obs-client"))
    implementation("io.ktor:ktor-server-core:1.6.0")
}

description = "connector-obs"
