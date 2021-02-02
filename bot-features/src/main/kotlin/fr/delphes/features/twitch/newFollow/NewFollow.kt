package fr.delphes.features.twitch.newFollow

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.TwitchChannel

class NewFollow(
    channel: TwitchChannel,
    val newFollowResponse: (NewFollow) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewFollowHandler())
    }

    inner class NewFollowHandler : EventHandler<NewFollow> {
        override suspend fun handle(event: NewFollow, bot: ClientBot): List<OutgoingEvent> = newFollowResponse(event)
    }
}