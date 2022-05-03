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

description = "bot-ui"
