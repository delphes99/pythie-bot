package fr.delphes.connector.twitch.statistics.event

import fr.delphes.connector.twitch.command.Command
import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
data class CommandAskTwitchStatistic(
    val user: UserName,
    val command: Command,
) : TwitchStatisticEventData