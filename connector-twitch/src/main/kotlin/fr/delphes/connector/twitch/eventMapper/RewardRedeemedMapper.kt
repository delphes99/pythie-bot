package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionEventPayload
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.User
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption as RewardRedemption1
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.RewardRedemption as RewardRedemptionTwitch

class RewardRedeemedMapper : TwitchIncomingEventMapper<ChannelPointsCustomRewardRedemptionEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelPointsCustomRewardRedemptionEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            RewardRedemption1(
                channel = channel,
                reward = RewardId(
                    twitchEvent.reward.id,
                    twitchEvent.reward.title
                ),
                user = User(twitchEvent.user_name),
                cost = twitchEvent.reward.cost,
            )
        )
    }
}