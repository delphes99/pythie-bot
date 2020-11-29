package fr.delphes.twitch.api.channelSubscribe.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelSubscribeCondition(
    val broadcaster_user_id: String
): GenericCondition