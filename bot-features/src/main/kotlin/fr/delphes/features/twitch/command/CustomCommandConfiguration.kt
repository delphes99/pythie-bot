@file:UseSerializers(DurationSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.dynamicForm.descriptor.OutgoingEventsFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.feature.FeatureConfiguration
import fr.delphes.feature.FeatureDescription
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
    private val channel: TwitchChannel = TwitchChannel(""),
    private val command: String = "",
    override val id: FeatureId = FeatureId(),
    private val cooldown: Duration = Duration.ZERO,
    private val actions: List<OutgoingEventBuilder> = emptyList(),
) : FeatureConfiguration {
    override fun buildFeature(): FeatureDefinition {
        return CustomCommand(
            channel = channel,
            trigger = command,
            id = id,
            cooldown = cooldown,
            action = {
                val outgoingEvent = actions.map { it.build() }
                outgoingEvent.forEach { this.executeOutgoingEvent(it) }
            }
        )
    }

    override suspend fun description(): FeatureDescription {
        return FeatureDescription(
            type = "TwitchCustomCommandConfiguration",
            id = id.value,
            descriptors = listOf(
                StringFieldDescriptor("channel", "Channel name", channel.name),
                StringFieldDescriptor("command", "command trigger", command),
                DurationFieldDescriptor("cooldown", "cooldown", cooldown),
                OutgoingEventsFieldDescriptor.fromBuilders("actions", "actions on trigger", actions),
            )
        )
    }
}