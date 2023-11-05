package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionEventPayload
import fr.delphes.twitch.api.user.UserName

class RewardRedeemedMapper : TwitchIncomingEventMapper<ChannelPointsCustomRewardRedemptionEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelPointsCustomRewardRedemptionEventPayload,
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            RewardRedemption(
                rewardId = RewardId(
                    channel = channel,
                    title = RewardTitle(twitchEvent.reward.title),
                ),
                user = UserName(twitchEvent.user_name),
                cost = twitchEvent.reward.cost,
            )
        )
    }
}