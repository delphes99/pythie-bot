plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":utils"))
    implementation(libs.ktor.server.core)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kitteh)
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(project(":utils-test"))
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

description = "twitch-api"
