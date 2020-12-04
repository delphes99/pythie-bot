package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption as RewardRedemptionTwitch

class RewardRedeemedHandler : TwitchIncomingEventHandler<RewardRedemptionTwitch> {
    override suspend fun handle(
        twitchEvent: RewardRedemptionTwitch,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        return listOf(
            RewardRedemption(
                twitchEvent
            )
        )
    }
}