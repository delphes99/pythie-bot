package fr.delphes.features.twitch.command

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.feature.ExperimentalFeature
import java.time.Duration
import java.util.UUID

data class ExperimentalTwitchCommand(
    val channel: String,
    val trigger: String,
    val cooldown: Duration,
    val responses: List<OutgoingEventBuilder>,
    override val id: String = UUID.randomUUID().toString()
): ExperimentalFeature<ExperimentalTwitchCommandRuntime> {
    override fun buildRuntime(): ExperimentalTwitchCommandRuntime? {
        return ExperimentalTwitchCommandRuntime(this)
    }
}