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
    inputs.dir("public")
    inputs.files(
        "index.html",
        "package.json",
        "tailwind.config.js",
        "tsconfig.json",
        "vitest.config.ts"
    )

    outputs.dir("dist")
}

description = "bot-ui"
