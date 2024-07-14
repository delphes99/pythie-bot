rootProject.name = "pythie-bot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://jcenter.bintray.com/")
        }
        maven {
            url = uri("https://maven.kotlindiscord.com/repository/maven-public/")
        }
    }
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":utils")
include(":utils-test")
include(":bot-core")

include(":annotation-generator")
include(":annotation")

include(":obs-client")
include(":twitch-api")

include(":connector-obs")
include(":connector-twitch")
include(":connector-discord")

include(":bot-ui")
include(":ui-overlay")

include(":bot-overlay")
include(":bot-features")
include(":pythie-bot")