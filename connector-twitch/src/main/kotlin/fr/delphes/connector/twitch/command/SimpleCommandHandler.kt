package fr.delphes.connector.twitch.command

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class SimpleCommandHandler(
    val channel: TwitchChannel,
    val command: Command,
    private var lastActivation: LocalDateTime = LocalDateTime.MIN,
    private val clock: Clock = SystemClock,
    private val cooldown: Duration? = null,
    private val responses: (CommandAsked) -> List<OutgoingEvent>
) : TwitchEventHandler<CommandAsked>(channel) {
    override suspend fun handleIfGoodChannel(event: CommandAsked, bot: Bot): List<OutgoingEvent> {
        return event.isFor(channel) {
            if(event.command == command &&
                cooldown?.let { Duration.between(lastActivation, clock.now()) > it } != false
            ) {
                lastActivation = clock.now()
                responses(event)
            } else {
                emptyList()
            }
        }
    }
}