plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":utils"))
    testImplementation(libs.bundles.kotlin.test)
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

description = "utils-test"
