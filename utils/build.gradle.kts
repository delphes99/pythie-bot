plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    testImplementation(libs.bundles.kotlin.test)
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

description = "fr.delphes.utils"
