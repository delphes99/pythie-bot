package fr.delphes.twitch.api.channelPrediction

import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionEndCondition
import fr.delphes.twitch.api.channelPrediction.payload.ChannelPredictionEndPayload
import fr.delphes.twitch.api.channelPrediction.payload.SubscribeChannelPredictionEnd
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelPredictionEndEventSubConfiguration :
    EventSubConfiguration<ChannelPredictionEndPayload, ChannelPredictionEndCondition>(
        EventSubTopic.CHANNEL_PREDICTION_END
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelPredictionEnd(
        ChannelPredictionEndCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPredictionEndPayload, ChannelPredictionEndCondition> {
        return call.receive()
    }
}