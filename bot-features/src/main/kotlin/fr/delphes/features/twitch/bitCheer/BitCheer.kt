package fr.delphes.features.twitch.bitCheer

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.TwitchFeature
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.twitch.TwitchChannel

class BitCheer(
    channel: TwitchChannel,
    val bitCheeredResponse: (BitCheered) -> List<OutgoingEvent>
) : TwitchFeature<BitCheerDescription>(channel) {
    override fun description() = BitCheerDescription(channel.name)

    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(BitCheeredHandler())
    }

    inner class BitCheeredHandler : TwitchEventHandler<BitCheered>(channel) {
        override suspend fun handleIfGoodChannel(event: BitCheered, bot: Bot): List<OutgoingEvent> {
            return bitCheeredResponse(event)
        }
    }
}