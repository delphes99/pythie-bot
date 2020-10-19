package fr.delphes.bot.twitch.handler

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class RewardRedeemedHandler : TwitchIncomingEventHandler<RewardRedeemedEvent> {
    override fun handle(
        twitchEvent: RewardRedeemedEvent,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        return listOf(RewardRedemption(twitchEvent))
    }
}