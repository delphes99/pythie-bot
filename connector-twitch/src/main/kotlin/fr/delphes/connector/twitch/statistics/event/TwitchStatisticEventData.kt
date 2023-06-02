package fr.delphes.connector.twitch.statistics.event

import fr.delphes.bot.connector.statistics.StatisticEventData
import kotlinx.serialization.Serializable

@Serializable
sealed interface TwitchStatisticEventData : StatisticEventData