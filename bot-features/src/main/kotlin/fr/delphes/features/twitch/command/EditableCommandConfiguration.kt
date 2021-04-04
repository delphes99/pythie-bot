package fr.delphes.features.twitch.command

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-command-editable")
data class EditableCommandConfiguration(
    override val channel: String,
    val trigger: String,
    val cooldown: Long,
    val responses: List<OutgoingEventBuilder>,
    override val id: String = UUID.randomUUID().toString()
) : TwitchFeatureDescription {
    constructor(
        channel: String,
        trigger: String,
        cooldown: Long,
        vararg responses: OutgoingEventBuilder,
    ) : this(channel, trigger, cooldown, listOf(*responses))
}