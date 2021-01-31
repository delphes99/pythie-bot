package fr.delphes.features.discord

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.incomingEvent.NewGuildMember
import fr.delphes.feature.AbstractFeature

class NewGuildMember(
    val newGuildMember: (NewGuildMember) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewGuildMemberHandler())
    }

    inner class NewGuildMemberHandler : EventHandler<NewGuildMember> {
        override suspend fun handle(event: NewGuildMember, channel: ChannelInfo): List<OutgoingEvent> =
            newGuildMember(event)
    }
}