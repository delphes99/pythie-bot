package fr.delphes.features.twitch.clipCreated

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel

class ClipCreated(
    override val channel: TwitchChannel,
    val clipCreatedResponse: (ClipCreated) -> List<OutgoingEvent>
) : NonEditableFeature<ClipCreatedDescription>, TwitchFeature {
    override fun description() = ClipCreatedDescription(channel.name)

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(ClipCreatedHandler())
        handlers
    }

    inner class ClipCreatedHandler : TwitchEventHandler<ClipCreated>(channel) {
        override suspend fun handleIfGoodChannel(event: ClipCreated, bot: Bot): List<OutgoingEvent> {
            return clipCreatedResponse(event)
        }
    }
}