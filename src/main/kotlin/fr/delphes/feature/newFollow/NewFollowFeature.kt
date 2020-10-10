package fr.delphes.feature.newFollow

import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class NewFollowFeature(
    val newFollowResponse: (NewFollow) -> List<OutgoingEvent>
) : AbstractFeature() {
    override val newFollowHandlers: List<EventHandler<NewFollow>> = listOf(NewFollowHandler())

    inner class NewFollowHandler : EventHandler<NewFollow> {
        override fun handle(event: NewFollow): List<OutgoingEvent> = newFollowResponse(event)
    }
}