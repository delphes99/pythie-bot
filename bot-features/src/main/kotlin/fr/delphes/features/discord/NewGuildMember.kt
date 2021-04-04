package fr.delphes.features.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.feature.Feature

class NewGuildMember(
    val newGuildMember: (NewGuildMember) -> List<OutgoingEvent>
) : Feature<NewGuildMemberDescription> {
    override fun description() = NewGuildMemberDescription()

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewGuildMemberHandler())
    }

    inner class NewGuildMemberHandler : EventHandler<NewGuildMember> {
        override suspend fun handle(event: NewGuildMember, bot: Bot): List<OutgoingEvent> =
            newGuildMember(event)
    }
}