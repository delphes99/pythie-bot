package fr.delphes.bot.command

import fr.delphes.User
import fr.delphes.bot.event.outgoing.OutgoingEvent

interface Command {
    val triggerMessage: String

    fun execute(user: User): List<OutgoingEvent>
}