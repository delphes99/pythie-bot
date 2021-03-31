package fr.delphes.features.twitch.command

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-command")
data class CommandDescription(
    override val channel: String,
    val trigger: String,
    override val id: String = UUID.randomUUID().toString()
) : TwitchFeatureDescription