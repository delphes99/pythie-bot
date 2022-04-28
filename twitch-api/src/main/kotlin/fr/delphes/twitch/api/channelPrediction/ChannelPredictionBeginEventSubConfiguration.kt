package fr.delphes.twitch.api.channelPrediction

import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionBeginCondition
import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionBeginPayload
import fr.delphes.twitch.api.channelPrediction.payload.SubscribeChannelPredictionBegin
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelPredictionBeginEventSubConfiguration :
    EventSubConfiguration<ChannelPredictionBeginPayload, ChannelPredictionBeginCondition>(
        EventSubTopic.CHANNEL_PREDICTION_BEGIN
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelPredictionBegin(
        ChannelPredictionBeginCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPredictionBeginPayload, ChannelPredictionBeginCondition> {
        return call.receive()
    }
}