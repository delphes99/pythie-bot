package fr.delphes.features.twitch.clipCreated

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.NonEditableTwitchFeature
import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.twitch.TwitchChannel

class ClipCreated(
    channel: TwitchChannel,
    val clipCreatedResponse: (ClipCreated) -> List<OutgoingEvent>
) : NonEditableTwitchFeature<ClipCreatedDescription>(channel) {
    override fun description() = ClipCreatedDescription(channel.name)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(ClipCreatedHandler())
    }

    inner class ClipCreatedHandler : TwitchEventHandler<ClipCreated>(channel) {
        override suspend fun handleIfGoodChannel(event: ClipCreated, bot: Bot): List<OutgoingEvent> {
            return clipCreatedResponse(event)
        }
    }
}