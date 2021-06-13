package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscriptionMessageCondition
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscriptionMessagePayload
import fr.delphes.twitch.api.channelSubscribe.payload.SubscribeChannelSubscriptionMessage
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubSubscribe
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelSubscriptionMessageEventSubConfiguration :
    EventSubConfiguration<NewSub, ChannelSubscriptionMessagePayload, ChannelSubscriptionMessageCondition>(
        EventSubTopic.CHANNEL_SUBSCRIPTION_MESSAGE
    ) {
    override fun transform(payload: ChannelSubscriptionMessagePayload, channel: TwitchChannel): NewSub {
        //TODO message
        return NewSub(
            channel,
            User(payload.user_name),
            payload.tier,
            false
        )
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): EventSubSubscribe<ChannelSubscriptionMessageCondition> {
        return SubscribeChannelSubscriptionMessage(
            ChannelSubscriptionMessageCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelSubscriptionMessagePayload, ChannelSubscriptionMessageCondition> {
        return call.receive()
    }
}