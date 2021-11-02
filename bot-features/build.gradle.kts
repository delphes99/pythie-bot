plugins {
    id("fr.delphes.kotlin-conventions")
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":bot-core"))
    api(project(":connector-discord"))
    api(project(":connector-obs"))
    api(project(":connector-twitch"))
    implementation("com.kennycason:kumo-core:1.27")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.10")
    testImplementation("org.assertj:assertj-core:3.17.2")
    testImplementation("io.mockk:mockk:1.12.0")
}

description = "bot-features"
