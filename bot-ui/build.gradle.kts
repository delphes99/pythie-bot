import com.github.gradle.node.npm.task.NpmInstallTask
import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

val copyBotApi = tasks.register<Copy>("copyBotApi") {
    dependsOn(gradle.includedBuild("bot-api").task(":assemble"))

    from("../bot-api/build/js") {
        exclude("**/node_modules")
    }
    into("./bot-api")
}

val buildTaskUsingYarn = tasks.register<NpmTask>("buildNpm") {
    dependsOn(copyBotApi)
    dependsOn(NpmInstallTask.NAME)

    npmCommand.set(listOf("run", "build"))
}

description = "bot-ui"
