package fr.delphes.twitch.api.channelPoll

import fr.delphes.twitch.api.channelPoll.payload.ChannelPollProgressCondition
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollProgressPayload
import fr.delphes.twitch.api.channelPoll.payload.SubscribeChannelPollProgress
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelPollProgressEventSubConfiguration :
    EventSubConfiguration<ChannelPollProgressPayload, ChannelPollProgressCondition>(
        EventSubTopic.CHANNEL_POLL_PROGRESS
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelPollProgress(
        ChannelPollProgressCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPollProgressPayload, ChannelPollProgressCondition> {
        return call.receive()
    }
}