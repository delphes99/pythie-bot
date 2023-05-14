@file:UseSerializers(DurationSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.feature.FeatureConfiguration
import fr.delphes.feature.FeatureDescription
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.OutgoingEventsFeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
@SerialName("TwitchCustomCommandConfiguration")
class CustomCommandConfiguration(
    private val channel: TwitchChannel,
    private val command: String,
    override val id: FeatureId,
    private val cooldown: Duration,
    private val actions: List<OutgoingEventBuilder> = emptyList(),
) : FeatureConfiguration {
    override fun buildFeature(): FeatureDefinition {
        return CustomCommand(
            channel = channel,
            trigger = command,
            id = id,
            cooldown = cooldown,
            action = {
                val outgoingEvent = actions.map { it.build(stateManager) }
                outgoingEvent.forEach { this.executeOutgoingEvent(it) }
            }
        )
    }

    override fun description(): FeatureDescription {
        return FeatureDescription(
            type = "TwitchCustomCommandConfiguration",
            id = id.value,
            descriptors = listOf(
                StringFeatureDescriptor("channel", "Channel name", channel.name),
                StringFeatureDescriptor("command", "command trigger", command),
                DurationFeatureDescriptor("cooldown", "cooldown", cooldown),
                OutgoingEventsFeatureDescriptor.fromBuilders("actions", "actions on trigger", actions),
            )
        )
    }
}