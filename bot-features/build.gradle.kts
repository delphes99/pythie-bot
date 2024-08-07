plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":bot-core"))
    api(project(":bot-overlay"))
    api(project(":connector-discord"))
    api(project(":connector-obs"))
    api(project(":connector-twitch"))
    implementation(libs.kumo)
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(project(":utils-test"))
    implementation(project(":annotation"))
    implementation(project(":annotation-generator"))
    ksp(project(":annotation-generator"))
}

ksp {
    arg("module-name", "features")
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

description = "bot-features"
