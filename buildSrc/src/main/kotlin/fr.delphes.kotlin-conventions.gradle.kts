plugins {
    `java-library`
    `maven-publish`
}

repositories {
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
