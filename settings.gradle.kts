rootProject.name = "pythie-bot"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":connector-obs")
includeBuild("bot-api") {
    dependencySubstitution {
        substitute(module("fr.delphes:bot-api")).using(project(":"))
    }
}
include(":bot-ui")
include(":bot-core")
include(":pythie-bot")
include(":connector-twitch")
include(":connector-discord")
include(":bot-features")
include(":obs-client")
include(":twitch-api")
include(":utils")