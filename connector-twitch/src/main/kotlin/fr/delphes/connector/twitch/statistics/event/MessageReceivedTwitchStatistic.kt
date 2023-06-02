package fr.delphes.connector.twitch.statistics.event

import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
data class MessageReceivedTwitchStatistic(
    val user: UserName,
    val text: String,
) : TwitchStatisticEventData