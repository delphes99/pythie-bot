package fr.delphes.connector.twitch.eventHandler

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption as RewardRedemptionTwitch

class RewardRedeemedHandler : TwitchIncomingEventHandler<RewardRedemptionTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption
    ): List<TwitchIncomingEvent> {
        return listOf(
            RewardRedemption(
                twitchEvent.channel,
                twitchEvent
            )
        )
    }
}