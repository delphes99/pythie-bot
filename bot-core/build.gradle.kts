plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    api(project(":utils"))
    implementation(project(":twitch-api"))
    api(libs.ktor.server.core)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(project(":utils-test"))
}

val copyUiTask = "copyBotUIToServer"
tasks.register<Copy>(copyUiTask) {
    dependsOn(":bot-ui:pnpmBuild")

    doFirst {
        delete(
            layout.buildDirectory.dir("resources/main/admin")
        )
    }

    from(layout.buildDirectory.dir("../../bot-ui/dist"))
    into(layout.buildDirectory.dir("resources/main/admin"))
}

val copyOverlayTask = "copyOverlayToServer"
tasks.register<Copy>(copyOverlayTask) {
    dependsOn(":bot-overlay:pnpmBuild")

    doFirst {
        delete(
            layout.buildDirectory.dir("resources/main/overlay")
        )
    }

    from(layout.buildDirectory.dir("../../bot-overlay/dist"))
    into(layout.buildDirectory.dir("resources/main/overlay"))
}

tasks.withType<Jar> {
    dependsOn(copyUiTask, copyOverlayTask)
}

description = "bot-core"