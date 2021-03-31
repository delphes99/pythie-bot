package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.SimpleCommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.FeatureDescription
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class Command(
    channel: TwitchChannel,
    private val trigger: String,
    lastActivation: LocalDateTime = LocalDateTime.MIN,
    clock: Clock = SystemClock,
    cooldown: Duration? = null,
    responses: List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun description(): FeatureDescription {
        return CommandDescription(
            channel.name,
            trigger
        )
    }

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = SimpleCommandHandler(
        channel,
        Command(trigger),
        lastActivation,
        clock,
        cooldown,
        responses
    )

    override val commands: Iterable<Command> = listOf(commandHandler.command)
}