package fr.delphes.features.twitch.voth

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-voth")
class VOTHDescription(
    override val channel: String,
    val reward: String,
    val statsCommand: String,
    val top3Command: String
): TwitchFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}