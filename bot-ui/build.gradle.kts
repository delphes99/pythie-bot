import com.github.gradle.node.npm.task.NpmInstallTask
import com.github.gradle.node.npm.task.NpmTask

plugins {
    alias(libs.plugins.node)
}


val buildTaskUsingYarn = tasks.register<NpmTask>("buildNpm") {
    dependsOn(tasks["npmInstall"])
    dependsOn(NpmInstallTask.NAME)

    npmCommand.set(listOf("run", "build"))
}

tasks["nodeSetup"].dependsOn(gradle.includedBuild("bot-sdk").task(":assemble"))

description = "bot-ui"
