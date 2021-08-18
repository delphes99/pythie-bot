import com.github.gradle.node.npm.task.NpmInstallTask
import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

val buildTaskUsingYarn = tasks.register<NpmTask>("buildNpm") {
    dependsOn(NpmInstallTask.NAME)
    npmCommand.set(listOf("run", "build"))
}

description = "bot-ui"
