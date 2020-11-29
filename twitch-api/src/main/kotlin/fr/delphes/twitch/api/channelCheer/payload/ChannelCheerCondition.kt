package fr.delphes.twitch.api.channelCheer.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelCheerCondition(
    val broadcaster_user_id: String
): GenericCondition