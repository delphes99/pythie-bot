package fr.delphes.features.twitch.newSub

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.NewSub as NewSubEvent

class NewSub(
    override val channel: TwitchChannel,
    val newSubResponse: (NewSubEvent) -> List<OutgoingEvent>
) : NonEditableFeature, TwitchFeature {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(NewSubHandler())
        .build()

    inner class NewSubHandler : TwitchEventHandler<NewSubEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: NewSubEvent, bot: Bot): List<OutgoingEvent> = newSubResponse(event)
    }
}