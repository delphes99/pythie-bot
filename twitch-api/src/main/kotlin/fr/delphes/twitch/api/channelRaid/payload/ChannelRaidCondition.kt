package fr.delphes.twitch.api.channelRaid.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelRaidCondition(
    val from_broadcaster_user_id: UserId? = null,
    val to_broadcaster_user_id: UserId? = null,
): GenericCondition