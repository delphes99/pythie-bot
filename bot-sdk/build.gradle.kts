plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

repositories {
    mavenCentral()
}

group = "fr.delphes"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(IR) {
        browser {
            version = "1.0.0"
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            binaries.library()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.client.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}