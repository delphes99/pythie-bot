package fr.delphes.connector.twitch.statistics.event

import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
data class UserFollowTwitchStatistic(
    val user: UserName,
) : TwitchStatisticEventData