package fr.delphes.twitch.api.channelUpdate.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelUpdateCondition(
    val broadcaster_user_id: UserId
) : GenericCondition