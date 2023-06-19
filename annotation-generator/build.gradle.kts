plugins {
    id("fr.delphes.kotlin-conventions")
    alias(libs.plugins.ksp)
    id(libs.plugins.kotlin.jvm.get().pluginId)
}

dependencies {
    implementation(libs.ksp)
    implementation(libs.kotlin.poet)
    implementation(project(":bot-core"))
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(libs.ksp.compile.testing)
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

description = "annotation-generator"