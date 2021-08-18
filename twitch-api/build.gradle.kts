plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":utils"))
    implementation("io.ktor:ktor-server-core:1.6.0")
    implementation("io.ktor:ktor-serialization:1.6.0")
    implementation("io.ktor:ktor-client:1.6.0")
    implementation("io.ktor:ktor-client-cio-jvm:1.6.0")
    implementation("io.ktor:ktor-client-serialization-jvm:1.6.0")
    implementation("io.ktor:ktor-client-logging-jvm:1.6.0")
    implementation("org.kitteh.irc:client-lib:7.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.10")
    testImplementation("org.assertj:assertj-core:3.17.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.0")
    testImplementation("io.kotest:kotest-assertions-json-jvm:4.6.0")
}

description = "twitch-api"
