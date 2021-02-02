package fr.delphes.features.twitch.bitCheer

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.BitCheered
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature
import fr.delphes.twitch.TwitchChannel

class BitCheer(
    channel: TwitchChannel,
    val bitCheeredResponse: (BitCheered) -> List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(BitCheeredHandler())
    }

    inner class BitCheeredHandler : EventHandler<BitCheered> {
        override suspend fun handle(event: BitCheered, bot: ClientBot): List<OutgoingEvent> = bitCheeredResponse(event)
    }
}