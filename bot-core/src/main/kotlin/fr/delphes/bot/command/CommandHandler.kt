package fr.delphes.bot.command

import fr.delphes.twitch.model.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.OutgoingEvent

class CommandHandler(
    val command: Command,
    private val doCommand: (User, ChannelInfo) -> List<OutgoingEvent>
) : EventHandler<CommandAsked> {
    override suspend fun handle(event: CommandAsked, channel: ChannelInfo): List<OutgoingEvent> {
        return if(event.command == command) {
            doCommand(event.by, channel)
        } else {
            emptyList()
        }
    }
}