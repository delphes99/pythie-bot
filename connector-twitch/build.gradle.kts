plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":bot-core"))
    //TODO >implementation : remove Twitch model from connector output ?
    api(project(":twitch-api"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.10")
    testImplementation("org.assertj:assertj-core:3.17.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.0")
    testImplementation("io.kotest:kotest-assertions-json-jvm:4.6.0")
    testImplementation("io.mockk:mockk:1.12.0")
}

description = "connector-twitch"
