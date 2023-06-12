plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    implementation(project(":obs-client"))
    implementation(libs.ktor.server.core)
    implementation(project(":annotation-generator"))
    ksp(project(":annotation-generator"))
}

description = "connector-obs"
