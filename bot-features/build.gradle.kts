plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":bot-core"))
    api(project(":connector-discord"))
    api(project(":connector-obs"))
    api(project(":connector-twitch"))
    implementation(libs.kumo)
    testImplementation(libs.bundles.kotlin.test)
}

description = "bot-features"
