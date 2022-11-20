package fr.delphes.connector.twitch.command

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

class LegacyCommandHandler(
    val channel: TwitchChannel,
    val command: Command,
    private val doCommand: suspend (User, TwitchConnector) -> List<OutgoingEvent>
) : TwitchEventHandler<CommandAsked>(channel) {
    override suspend fun handleIfGoodChannel(event: CommandAsked, bot: Bot): List<OutgoingEvent> {
        val twitchConnector = bot.connectors.filterIsInstance<TwitchConnector>().first()
        return if(event.command == command) {
            doCommand(event.by, twitchConnector)
        } else {
            emptyList()
        }
    }
}