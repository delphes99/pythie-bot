package fr.delphes.bot.command

import fr.delphes.User
import fr.delphes.bot.Channel
import fr.delphes.bot.event.outgoing.OutgoingEvent

class CommandHandler(
    override val triggerMessage: String,
    private val doCommand: (User, Channel) -> List<OutgoingEvent>
) : Command {
    override fun execute(user: User, channel: Channel): List<OutgoingEvent> {
        return doCommand(user, channel)
    }
}