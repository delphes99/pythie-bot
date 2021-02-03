package fr.delphes.features.twitch.clipCreated

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.ClipCreated
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.TwitchChannel

class ClipCreated(
    channel: TwitchChannel,
    val clipCreatedResponse: (ClipCreated) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(ClipCreatedHandler())
    }

    inner class ClipCreatedHandler : EventHandler<ClipCreated> {
        override suspend fun handle(event: ClipCreated, bot: ClientBot): List<OutgoingEvent> = clipCreatedResponse(event)
    }
}