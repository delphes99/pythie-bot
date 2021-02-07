package fr.delphes.features.twitch.newFollow

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.NewFollow as NewFollowEvent

class NewFollow(
    channel: TwitchChannel,
    val newFollowResponse: (NewFollowEvent) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(NewFollowHandler())
    }

    inner class NewFollowHandler : TwitchEventHandler<NewFollowEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: NewFollowEvent, bot: Bot): List<OutgoingEvent> = newFollowResponse(event)
    }
}