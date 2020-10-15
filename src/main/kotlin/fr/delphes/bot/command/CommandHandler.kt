package fr.delphes.bot.command

import fr.delphes.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.OutgoingEvent

class CommandHandler(
    override val triggerMessage: String,
    private val doCommand: (User, ChannelInfo) -> List<OutgoingEvent>
) : Command, EventHandler<CommandAsked> {
    override fun handle(event: CommandAsked, channel: ChannelInfo): List<OutgoingEvent> {
        return if(event.command == this) {
            doCommand(event.by, channel)
        } else {
            emptyList()
        }
    }
}