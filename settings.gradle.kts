rootProject.name = "pythie-bot"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":utils")
include(":utils-test")
include(":bot-core")
include("bot-overlay")
include(":bot-features")
include(":pythie-bot")

include(":connector-obs")
include(":connector-twitch")
include(":connector-discord")

include(":obs-client")
include(":twitch-api")

include(":bot-ui")
include(":ui-overlay")
include(":annotation-generator")
include(":annotation")