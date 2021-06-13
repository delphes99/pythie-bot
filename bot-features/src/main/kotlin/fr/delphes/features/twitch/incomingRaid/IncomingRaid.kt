package fr.delphes.features.twitch.incomingRaid

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel

class IncomingRaid(
    override val channel: TwitchChannel,
    val incomingRaidResponse: (IncomingRaid) -> List<OutgoingEvent>
) : NonEditableFeature<IncomingRaidDescription>, TwitchFeature {
    override fun description() = IncomingRaidDescription(channel.name)

    override val eventHandlers = run {
        val handlers = EventHandlers()
        handlers.addHandler(IncomingRaidHandler())
        handlers
    }

    private inner class IncomingRaidHandler : TwitchEventHandler<IncomingRaid>(channel) {
        override suspend fun handleIfGoodChannel(event: IncomingRaid, bot: Bot): List<OutgoingEvent> = incomingRaidResponse(event)
    }
}