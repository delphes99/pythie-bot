package fr.delphes.feature.bitCheer

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.BitCheered
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature

class BitCheer(
    val bitCheeredResponse: (BitCheered) -> List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(BitCheeredHandler())
    }

    inner class BitCheeredHandler : EventHandler<BitCheered> {
        override fun handle(event: BitCheered, channel: ChannelInfo): List<OutgoingEvent> = bitCheeredResponse(event)
    }
}