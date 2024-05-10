import com.github.gradle.node.pnpm.task.PnpmTask
import org.gradle.api.internal.TaskInputsInternal

plugins {
    alias(libs.plugins.node)
}

node {
    version.set(libs.versions.node.version)
    download.set(true)
    pnpmVersion.set(libs.versions.pnpm.version)
}

tasks.register<PnpmTask>("pnpmBuild") {
    dependsOn(tasks.pnpmInstall)

    args.set(listOf("run", "build"))

    outputs.cacheIf { true }

    inputs.addFilesFromSource()

    outputs.dir("dist")
}

val testTask = tasks.register<PnpmTask>("pnpmTest") {
    dependsOn(tasks.pnpmInstall)

    inputs.addFilesFromSource()

    args.set(listOf("run", "test"))
}

tasks.withType<Test> {
    dependsOn(testTask)
}

description = "bot-ui"

fun TaskInputsInternal.addFilesFromSource() {
    dir("node_modules")
    dir("src")
    dir("public")
    files(
        "index.html",
        "package.json",
        "tailwind.config.js",
        "tsconfig.json",
        "vitest.config.ts"
    )
}