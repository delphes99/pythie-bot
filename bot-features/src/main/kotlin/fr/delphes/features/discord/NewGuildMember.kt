package fr.delphes.features.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandler
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.feature.NonEditableFeature

class NewGuildMember(
    val newGuildMember: (NewGuildMember) -> List<OutgoingEvent>
) : NonEditableFeature {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(NewGuildMemberHandler())
        .build()

    inner class NewGuildMemberHandler : LegacyEventHandler<NewGuildMember> {
        override suspend fun handle(event: NewGuildMember, bot: Bot): List<OutgoingEvent> =
            newGuildMember(event)
    }
}