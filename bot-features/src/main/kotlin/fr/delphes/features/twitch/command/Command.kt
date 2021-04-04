package fr.delphes.features.twitch.command

import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.NonEditableTwitchFeature
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.command.SimpleCommandHandler
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration

class Command(
    channel: TwitchChannel,
    private val trigger: String,
    clock: Clock = SystemClock,
    private val cooldown: Duration? = null,
    responses: (CommandAsked) -> List<OutgoingEvent>
) : NonEditableTwitchFeature<CommandDescription>(channel) {
    override fun description() = CommandDescription(
        channel.name,
        trigger,
        cooldown?.let { cooldown.toSeconds() } ?: 0
    )

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = SimpleCommandHandler(
        channel,
        Command(trigger),
        clock,
        cooldown,
        responses
    )

    override val commands: Iterable<Command> = listOf(commandHandler.command)
}