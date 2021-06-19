package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPollBeginCondition(
    val broadcaster_user_id: String
): GenericCondition