plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    implementation("net.dv8tion:JDA:4.2.0_222")
    implementation("io.ktor:ktor-server-core:1.6.0")
}

description = "connector-discord"
