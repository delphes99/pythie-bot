package fr.delphes.features.twitch.command

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.SimpleCommandHandler
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration

class Command(
    override val channel: TwitchChannel,
    private val trigger: String,
    private val clock: Clock = SystemClock,
    private val cooldown: Duration? = null,
    private val responses: (CommandAsked) -> List<OutgoingEvent>
) : NonEditableFeature<CommandDescription>, TwitchFeature {
    override fun description() = CommandDescription(
        channel.name,
        trigger,
        cooldown?.let { cooldown.toSeconds() } ?: 0
    )

    private val command = Command(trigger)

    override val eventHandlers = EventHandlers
        .builder()
        .addHandler(buildCommandHandler())
        .build()

    private fun buildCommandHandler() = SimpleCommandHandler(
        channel,
        command,
        clock,
        cooldown,
        responses
    )

    override val commands: Iterable<Command> = listOf(command)
}