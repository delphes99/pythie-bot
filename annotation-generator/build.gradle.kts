import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.ksp)
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

dependencies {
    implementation(libs.bundles.kotlin.common)
    implementation(libs.ksp)
    implementation(libs.kotlin.poet)
    implementation(project(":annotation"))
    implementation(project(":bot-core"))
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(libs.bundles.compile.test)
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.withType<KotlinCompile>() {
    compilerOptions {
        freeCompilerArgs.add("-Xopt-in=com.google.devtools.ksp.KspExperimental")
        if (name == "compileTestKotlin") {
            freeCompilerArgs.add("-Xopt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

description = "annotation-generator"