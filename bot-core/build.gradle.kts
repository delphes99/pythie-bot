plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    api(project(":utils"))
    implementation(project(":twitch-api"))
    api("io.ktor:ktor-server-core:1.6.0")
    implementation("io.ktor:ktor-server-netty:1.6.0")
    implementation("io.ktor:ktor-serialization:1.6.0")
    implementation("io.ktor:ktor-websockets:1.6.0")
    api("io.ktor:ktor-client:1.6.0")
    implementation("fr.delphes:bot-api")
    implementation("io.ktor:ktor-client-apache:1.6.0")
    implementation("io.ktor:ktor-client-serialization:1.6.0")
    implementation("io.ktor:ktor-client-serialization-jvm:1.6.0")
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