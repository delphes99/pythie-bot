package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionCondition
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionEventPayload
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.SubscribechannelPointsCustomRewardRedemption
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class CustomRewardRedemptionEventSubConfiguration :
    EventSubConfiguration<
            ChannelPointsCustomRewardRedemptionEventPayload,
            ChannelPointsCustomRewardRedemptionCondition>(
        EventSubTopic.CUSTOM_REWARD_REDEMPTION
    ) {

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribechannelPointsCustomRewardRedemption(
        ChannelPointsCustomRewardRedemptionCondition(userId),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPointsCustomRewardRedemptionEventPayload, ChannelPointsCustomRewardRedemptionCondition> {
        return call.receive()
    }
}