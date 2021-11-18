plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(project(":utils"))
    implementation(libs.ktor.server.core)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kitteh)
    testImplementation(libs.bundles.kotlin.test)
}

description = "twitch-api"
