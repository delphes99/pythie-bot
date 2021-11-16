import com.github.gradle.node.npm.task.NpmInstallTask
import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}


val buildTaskUsingYarn = tasks.register<NpmTask>("buildNpm") {
    dependsOn(tasks["npmInstall"])
    dependsOn(NpmInstallTask.NAME)

    npmCommand.set(listOf("run", "build"))
}

tasks["nodeSetup"].dependsOn(gradle.includedBuild("bot-sdk").task(":assemble"))

description = "bot-ui"
