rootProject.name = "pythie-bot"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":utils")
include(":bot-core")
include(":bot-features")
include(":pythie-bot")

include(":connector-obs")
include(":connector-twitch")
include(":connector-discord")

include(":obs-client")
include(":twitch-api")

include(":bot-ui")
include(":bot-overlay")