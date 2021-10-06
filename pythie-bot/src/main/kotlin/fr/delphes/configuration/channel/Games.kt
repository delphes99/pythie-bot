package fr.delphes.configuration.channel

import fr.delphes.twitch.api.games.WithGameId

enum class Games(override val id: String) : WithGameId {
    SCIENCE_TECHNOLOGY("509670"),
    SOFTWARE_DEVELOPMENT("1469308723"),
    JUST_CHATTING("509658"),
    SATISFACTORY("506456"),
    PATH_OF_EXILE("29307"),
    GEOGUESSR("369418"),
    GHOSTRUNNER("513971"),
}