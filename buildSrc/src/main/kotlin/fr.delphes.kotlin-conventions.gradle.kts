plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://jcenter.bintray.com/")
    }
    maven {
        url = uri("https://maven.kotlindiscord.com/repository/maven-public/")
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.30")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.30")
}

group = "fr.delphes"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
