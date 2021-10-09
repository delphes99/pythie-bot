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
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.10")
    testImplementation("org.assertj:assertj-core:3.17.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.0")
    testImplementation("io.kotest:kotest-assertions-json-jvm:4.6.0")
    testImplementation("io.mockk:mockk:1.11.0")
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