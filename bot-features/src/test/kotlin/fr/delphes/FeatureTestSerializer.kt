package fr.delphes

import fr.delphes.bot.buildSerializer
import fr.delphes.connector.discord.DiscordInitializer
import fr.delphes.connector.obs.ObsInitializer
import fr.delphes.connector.twitch.TwitchInitializer
import fr.delphes.features.featureSerializersModule

private val connectors = listOf(
    DiscordInitializer(),
    ObsInitializer(),
    TwitchInitializer()
)

val serializer = buildSerializer(
    featureSerializersModule,
    connectors
)