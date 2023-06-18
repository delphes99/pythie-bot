plugins {
    id("fr.delphes.kotlin-conventions")
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    alias(libs.plugins.ksp)
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
    implementation(project(":annotation-generator"))
    ksp(project(":annotation-generator"))
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

val copyUiOverlayTask = "copyUiOverlayToServer"
tasks.register<Copy>(copyUiOverlayTask) {
    dependsOn(":ui-overlay:pnpmBuild")

    doFirst {
        delete(
            layout.buildDirectory.dir("resources/main/overlay")
        )
    }

    from(layout.buildDirectory.dir("../../ui-overlay/dist"))
    into(layout.buildDirectory.dir("resources/main/overlay"))
}

tasks.withType<Jar> {
    dependsOn(copyUiTask, copyUiOverlayTask)
}

ksp {
    arg("module-name", "core")
}

description = "bot-core"