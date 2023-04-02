package fr.delphes.twitch.api.channelPrediction.payload

import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPredictionEndCondition(
    val broadcaster_user_id: UserId
): GenericCondition