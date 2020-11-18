package fr.delphes.bot.twitch.handler

import com.github.twitch4j.pubsub.events.ChannelBitsEvent
import fr.delphes.twitch.model.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.BitCheered
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class ChannelBitsHandler : TwitchIncomingEventHandler<ChannelBitsEvent> {
    override fun handle(
        twitchEvent: ChannelBitsEvent,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        return listOf(
            BitCheered(
                User(twitchEvent.data.userName),
                twitchEvent.data.bitsUsed.toLong()
            )
        )
    }
}