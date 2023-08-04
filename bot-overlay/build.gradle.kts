plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    alias(libs.plugins.ksp)
}


dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":bot-core"))
    api(libs.ktor.server.core)
    implementation(libs.bundles.ktor.server)
    implementation(project(":annotation-generator"))
    ksp(project(":annotation-generator"))
}

ksp {
    arg("module-name", "overlay")
}