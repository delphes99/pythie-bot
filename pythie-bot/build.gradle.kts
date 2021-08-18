plugins {
    id("fr.delphes.kotlin-conventions")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":bot-core"))
    implementation(project(":bot-features"))
}

description = "pythie-bot"
