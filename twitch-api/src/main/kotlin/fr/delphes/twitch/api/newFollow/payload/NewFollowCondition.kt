package fr.delphes.twitch.api.newFollow.payload

import fr.delphes.twitch.eventSub.payload.GenericCondition
import kotlinx.serialization.Serializable

@Serializable
data class NewFollowCondition(
    val broadcaster_user_id: String
): GenericCondition