import com.github.gradle.node.pnpm.task.PnpmTask

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

    inputs.dir("node_modules")
    inputs.dir("src")
    inputs.files(
        "package.json",
        "tailwind.config.js",
        "index.html"
    )

    outputs.dir("dist")
}

description = "ui-overlay"
