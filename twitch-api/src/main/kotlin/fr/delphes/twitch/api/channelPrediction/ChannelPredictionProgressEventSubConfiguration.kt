package fr.delphes.twitch.api.channelPrediction

import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionProgressCondition
import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionProgressPayload
import fr.delphes.twitch.api.channelPrediction.payload.SubscribeChannelPredictionProgress
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelPredictionProgressEventSubConfiguration :
    EventSubConfiguration<ChannelPredictionProgressPayload, ChannelPredictionProgressCondition>(
        EventSubTopic.CHANNEL_PREDICTION_PROGRESS
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelPredictionProgress(
        ChannelPredictionProgressCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPredictionProgressPayload, ChannelPredictionProgressCondition> {
        return call.receive()
    }
}