package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionCondition
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionEventPayload
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.SubscribechannelPointsCustomRewardRedemption
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class CustomRewardRedemptionEventSubConfiguration(
    channel: TwitchChannel,
    listener: (RewardRedemption) -> Unit,
    private val rewardsConfigurations: List<RewardConfiguration>
) : EventSubConfiguration<RewardRedemption,
        ChannelPointsCustomRewardRedemptionEventPayload,
        ChannelPointsCustomRewardRedemptionCondition>(
    channel,
    "customRewardRedemption",
    listener
) {
    override fun transform(
        payload: ChannelPointsCustomRewardRedemptionEventPayload
    ): RewardRedemption? {
        val configuration = rewardsConfigurations
            .find { configuration -> configuration.title == payload.reward.title }
            ?: return null

        return RewardRedemption(
            channel = channel,
            reward = Reward(
                payload.reward.id,
                configuration
            ),
            user = User(payload.user_name),
            cost = payload.reward.cost,
            id = payload.id
        )
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribechannelPointsCustomRewardRedemption {
        return SubscribechannelPointsCustomRewardRedemption(
            ChannelPointsCustomRewardRedemptionCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPointsCustomRewardRedemptionEventPayload, ChannelPointsCustomRewardRedemptionCondition> {
        return call.receive()
    }
}