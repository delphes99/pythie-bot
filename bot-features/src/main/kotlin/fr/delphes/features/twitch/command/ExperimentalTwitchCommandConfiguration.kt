@file:UseSerializers(DurationSerializer::class)

package fr.delphes.features.twitch.command

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.feature.ExperimentalFeatureConfiguration
import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.UseSerializers
import java.time.Duration

@kotlinx.serialization.Serializable
class ExperimentalTwitchCommandConfiguration(
    override val id: String,
    val channel: String?,
    val trigger: String?,
    val cooldown: Duration?,
    val responses: List<OutgoingEventBuilder> = emptyList(),
) : ExperimentalFeatureConfiguration<ExperimentalTwitchCommand> {
    override fun buildFeature(id: String): ExperimentalTwitchCommand? {
        if (channel == null || trigger == null || cooldown == null || responses.isEmpty()) {
            return null
        }

        return ExperimentalTwitchCommand(
            channel,
            trigger,
            cooldown,
            responses
        )
    }
}