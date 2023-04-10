@file:UseSerializers(DurationSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.feature.FeatureConfiguration
import fr.delphes.rework.feature.CustomFeature
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
class CustomCommandConfiguration(
    private val channel: TwitchChannel,
    private val command: String,
    override val id: FeatureId,
    private val cooldown: Duration,
): FeatureConfiguration {
    override fun buildFeature(): CustomFeature {
        return CustomCommand(
            channel = channel,
            trigger = command,
            id = id,
            cooldown = cooldown,
            action = {
                //TODO
                println("Custom command $command executed on channel $channel")
            }
        )
    }
}