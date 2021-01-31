package fr.delphes.features.twitch.newSub

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature

class NewSub(
    channel: String,
    val newSubResponse: (NewSub) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewSubHandler())
    }

    inner class NewSubHandler() : EventHandler<NewSub> {
        override suspend fun handle(event: NewSub, channel: ChannelInfo): List<OutgoingEvent> = newSubResponse(event)
    }
}