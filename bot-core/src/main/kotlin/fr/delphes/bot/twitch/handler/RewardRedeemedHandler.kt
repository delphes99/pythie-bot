package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.RewardRedemption
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class RewardRedeemedHandler : TwitchIncomingEventHandler<fr.delphes.twitch.api.reward.RewardRedemption> {
    override fun handle(
        twitchEvent: fr.delphes.twitch.api.reward.RewardRedemption,
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