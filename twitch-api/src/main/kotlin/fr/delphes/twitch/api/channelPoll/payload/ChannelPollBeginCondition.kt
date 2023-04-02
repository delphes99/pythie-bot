package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPollBeginCondition(
    val broadcaster_user_id: UserId
): GenericCondition