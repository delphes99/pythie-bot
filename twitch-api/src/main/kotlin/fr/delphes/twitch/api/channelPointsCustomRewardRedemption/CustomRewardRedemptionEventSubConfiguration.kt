package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionCondition
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionEventPayload
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.SubscribechannelPointsCustomRewardRedemption
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class CustomRewardRedemptionEventSubConfiguration(
    listener: (RewardRedemption) -> Unit
) : EventSubConfiguration<RewardRedemption,
        ChannelPointsCustomRewardRedemptionEventPayload,
        ChannelPointsCustomRewardRedemptionCondition>(
    "customRewardRedemption",
    listener
) {
    override fun transform(
        payload: ChannelPointsCustomRewardRedemptionEventPayload
    ): RewardRedemption {
        return RewardRedemption(
            Reward(
                payload.reward.id,
                payload.reward.title
            ),
            User(payload.user_name),
            payload.reward.cost
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