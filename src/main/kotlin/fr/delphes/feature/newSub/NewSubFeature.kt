package fr.delphes.feature.newSub

import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class NewSubFeature(
    val newSubResponse: (NewSub) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewSubHandler())
    }

    inner class NewSubHandler() : EventHandler<NewSub> {
        override fun handle(event: NewSub): List<OutgoingEvent> = newSubResponse(event)
    }
}