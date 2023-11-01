package fr.delphes.twitch.api.channelFollow.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelFollowCondition(
    val broadcaster_user_id: UserId,
    val moderator_user_id: UserId = broadcaster_user_id,
) : GenericCondition