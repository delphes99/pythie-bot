@file:UseSerializers(DurationSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.feature.FeatureConfiguration
import fr.delphes.feature.FeatureDescription
import fr.delphes.feature.FeatureDescriptionType
import fr.delphes.feature.FeatureDescriptor
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
) : FeatureConfiguration {
    override fun buildFeature(): FeatureDefinition {
        return CustomCommand(
            channel = channel,
            trigger = command,
            id = id,
            cooldown = cooldown,
            action = {
                //TODO implement
                println("Custom command $command executed on channel $channel")
            }
        )
    }

    override fun description(): FeatureDescription {
        return FeatureDescription(
            type = "TwitchCustomCommandConfiguration",
            id = id.value,
            descriptors = listOf(
                FeatureDescriptor("channel", "Channel name", FeatureDescriptionType.STRING, channel.name),
                FeatureDescriptor("command", "command", FeatureDescriptionType.STRING, command),
                FeatureDescriptor("cooldown", "cooldown", FeatureDescriptionType.STRING, cooldown.toString()),
            )
        )
    }
}