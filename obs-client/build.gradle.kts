plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    implementation(libs.bundles.ktor.client)
    testImplementation(libs.bundles.kotlin.test)
}

description = "obs-client"
