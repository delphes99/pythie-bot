package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPollEndCondition(
    val broadcaster_user_id: UserId
): GenericCondition