package fr.delphes.twitch.api.channelPrediction.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class ChannelPredictionLockCondition(
    val broadcaster_user_id: String
): GenericCondition