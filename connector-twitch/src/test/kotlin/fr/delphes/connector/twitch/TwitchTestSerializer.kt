package fr.delphes.connector.twitch

import fr.delphes.bot.buildSerializer

val twitchTestSerializer = buildSerializer(
    TwitchInitializer()
)