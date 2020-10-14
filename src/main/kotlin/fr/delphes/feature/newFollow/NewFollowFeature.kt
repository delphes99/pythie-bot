package fr.delphes.feature.newFollow

import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class NewFollowFeature(
    val newFollowResponse: (NewFollow) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewFollowHandler())
    }

    inner class NewFollowHandler : EventHandler<NewFollow> {
        override fun handle(event: NewFollow): List<OutgoingEvent> = newFollowResponse(event)
    }
}