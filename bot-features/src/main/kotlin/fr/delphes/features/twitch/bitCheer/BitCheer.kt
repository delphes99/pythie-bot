package fr.delphes.features.twitch.bitCheer

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.LegacyEventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.feature.NonEditableFeature
import fr.delphes.twitch.TwitchChannel

class BitCheer(
    override val channel: TwitchChannel,
    val bitCheeredResponse: (BitCheered) -> List<OutgoingEvent>
) : NonEditableFeature, TwitchFeature {
    override val eventHandlers = LegacyEventHandlers
        .builder()
        .addHandler(BitCheeredHandler())
        .build()

    inner class BitCheeredHandler : TwitchEventHandler<BitCheered>(channel) {
        override suspend fun handleIfGoodChannel(event: BitCheered, bot: Bot): List<OutgoingEvent> {
            return bitCheeredResponse(event)
        }
    }
}