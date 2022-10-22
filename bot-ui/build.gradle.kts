import com.github.gradle.node.npm.task.NpmInstallTask
import com.github.gradle.node.npm.task.NpmTask

plugins {
    alias(libs.plugins.node)
}


val buildTaskUsingYarn = tasks.register<NpmTask>("buildNpm") {
    dependsOn(tasks["npmInstall"])
    dependsOn(NpmInstallTask.NAME)

    inputs.files(fileTree("node_modules"))
    inputs.files(fileTree("src"))
    inputs.files(fileTree("public"))
    inputs.file("package.json")
    inputs.file("tailwind.config.js")
    inputs.file("index.html")

    outputs.dir("dist")

    npmCommand.set(listOf("run", "build"))
}

description = "bot-ui"
