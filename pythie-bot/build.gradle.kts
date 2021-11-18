plugins {
    id("fr.delphes.kotlin-conventions")
    alias(libs.plugins.fatjar)
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(project(":bot-core"))
    implementation(project(":bot-features"))
}

description = "pythie-bot"
