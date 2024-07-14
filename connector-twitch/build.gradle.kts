plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    //TODO >implementation : remove Twitch model from connector output ?
    api(project(":twitch-api"))
    implementation(libs.ktor.client.core)
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(project(":utils-test"))
    implementation(project(":annotation"))
    implementation(project(":annotation-generator"))
    ksp(project(":annotation-generator"))
}

ksp {
    arg("module-name", "twitch")
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

description = "connector-twitch"
