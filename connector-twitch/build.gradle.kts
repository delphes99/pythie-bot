plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    //TODO >implementation : remove Twitch model from connector output ?
    api(project(":twitch-api"))
    implementation(libs.ktor.client.core)
    testImplementation(libs.bundles.kotlin.test)
}

description = "connector-twitch"
