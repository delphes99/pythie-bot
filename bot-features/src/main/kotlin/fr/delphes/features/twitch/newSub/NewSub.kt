package fr.delphes.features.twitch.newSub

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.NewSub as NewSubEvent

class NewSub(
    channel: TwitchChannel,
    val newSubResponse: (NewSubEvent) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewSubHandler())
    }

    inner class NewSubHandler : TwitchEventHandler<NewSubEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: NewSubEvent, bot: Bot): List<OutgoingEvent> = newSubResponse(event)
    }
}