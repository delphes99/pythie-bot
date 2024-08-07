plugins {
    alias(libs.plugins.fatjar)
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(project(":bot-core"))
    implementation(project(":bot-features"))
}

tasks.withType<Jar> {
    archiveBaseName.set("pythie-bot")
    archiveClassifier.set("")
    archiveVersion.set("")

    manifest {
        attributes(
            mapOf(
                "Main-Class" to "fr.delphes.LaunchBotKt",
                "Description" to "PythieBot - Streamer tool",
            )
        )
    }
}

tasks.withType<Test>() {
    useJUnitPlatform()
}

description = "pythie-bot"