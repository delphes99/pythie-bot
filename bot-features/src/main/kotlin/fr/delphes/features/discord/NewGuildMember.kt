package fr.delphes.features.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.feature.NonEditableFeature

class NewGuildMember(
    val newGuildMember: (NewGuildMember) -> List<OutgoingEvent>
) : NonEditableFeature<NewGuildMemberDescription> {
    override fun description() = NewGuildMemberDescription()

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(NewGuildMemberHandler())
        handlers
    }

    inner class NewGuildMemberHandler : EventHandler<NewGuildMember> {
        override suspend fun handle(event: NewGuildMember, bot: Bot): List<OutgoingEvent> =
            newGuildMember(event)
    }
}