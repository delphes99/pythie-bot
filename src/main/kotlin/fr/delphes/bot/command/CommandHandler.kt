package fr.delphes.bot.command

import fr.delphes.User
import fr.delphes.bot.event.outgoing.OutgoingEvent

class CommandHandler(
    override val triggerMessage: String,
    private val doCommand: (user: User) -> List<OutgoingEvent>
) : Command {
    override fun execute(user: User): List<OutgoingEvent> {
        return doCommand(user)
    }
}