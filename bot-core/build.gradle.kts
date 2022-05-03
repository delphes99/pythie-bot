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

tasks.register<Copy>("copyFrontToServer") {
    dependsOn(":bot-ui:buildNpm")
    from(layout.buildDirectory.dir("../../bot-ui/dist"))
    into(layout.buildDirectory.dir("resources/main/admin"))
}

tasks.withType<Jar> {
    dependsOn("copyFrontToServer")
}

description = "bot-core"