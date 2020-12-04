package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.BitCheered
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelCheer.NewCheer

class ChannelBitsHandler : TwitchIncomingEventHandler<NewCheer> {
    override suspend fun handle(
        twitchEvent: NewCheer,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        return listOf(
            BitCheered(
                twitchEvent.cheerer,
                twitchEvent.bits,
                twitchEvent.message
            )
        )
    }
}