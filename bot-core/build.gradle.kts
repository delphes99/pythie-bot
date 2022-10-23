plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    api(project(":utils"))
    implementation(project(":twitch-api"))
    api(libs.ktor.server.core)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    testImplementation(libs.bundles.kotlin.test)
}

tasks.register<Copy>("copyBotUIToServer") {
    dependsOn(":bot-ui:buildNpm")

    doFirst {
        delete(
            layout.buildDirectory.dir("resources/main/admin")
        )
    }

    from(layout.buildDirectory.dir("../../bot-ui/dist"))
    into(layout.buildDirectory.dir("resources/main/admin"))
}

tasks.register<Copy>("copyOverlayToServer") {
    dependsOn(":bot-overlay:buildNpm")

    doFirst {
        delete(
            layout.buildDirectory.dir("resources/main/overlay")
        )
    }

    from(layout.buildDirectory.dir("../../bot-overlay/dist"))
    into(layout.buildDirectory.dir("resources/main/overlay"))
}

tasks.withType<Jar> {
    dependsOn("copyBotUIToServer", "copyOverlayToServer")
}

description = "bot-core"