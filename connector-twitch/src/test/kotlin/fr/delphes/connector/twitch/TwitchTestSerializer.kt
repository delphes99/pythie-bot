package fr.delphes.connector.twitch

import fr.delphes.bot.buildSerializer
import fr.delphes.connector.twitch.reward.ConfiguredRewards

val twitchTestSerializer = buildSerializer(
    TwitchInitializer(
        listOf(),
        ConfiguredRewards()
    )
)