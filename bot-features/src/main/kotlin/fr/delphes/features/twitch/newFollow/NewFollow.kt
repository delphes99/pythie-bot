package fr.delphes.features.twitch.newFollow

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel
import fr.delphes.connector.twitch.incomingEvent.NewFollow as NewFollowEvent

class NewFollow(
    override val channel: TwitchChannel,
    val newFollowResponse: (NewFollowEvent) -> List<OutgoingEvent>
) : NonEditableFeature<NewFollowDescription>, TwitchFeature {
    override fun description() = NewFollowDescription(channel.name)

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(NewFollowHandler())
        handlers
    }

    inner class NewFollowHandler : TwitchEventHandler<NewFollowEvent>(channel) {
        override suspend fun handleIfGoodChannel(event: NewFollowEvent, bot: Bot): List<OutgoingEvent> = newFollowResponse(event)
    }
}