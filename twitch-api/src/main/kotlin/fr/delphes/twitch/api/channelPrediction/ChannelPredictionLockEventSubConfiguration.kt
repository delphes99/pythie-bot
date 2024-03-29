package fr.delphes.twitch.api.channelPrediction

import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionLockCondition
import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionLockPayload
import fr.delphes.twitch.api.channelPrediction.payload.SubscribeChannelPredictionLock
import fr.delphes.twitch.api.user.UserId
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelPredictionLockEventSubConfiguration :
    EventSubConfiguration<ChannelPredictionLockPayload, ChannelPredictionLockCondition>(
        EventSubTopic.CHANNEL_PREDICTION_LOCK
    ) {
    override fun subscribePayload(
        userId: UserId,
        transport: SubscribeTransport
    ) = SubscribeChannelPredictionLock(
        ChannelPredictionLockCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPredictionLockPayload, ChannelPredictionLockCondition> {
        return call.receive()
    }
}