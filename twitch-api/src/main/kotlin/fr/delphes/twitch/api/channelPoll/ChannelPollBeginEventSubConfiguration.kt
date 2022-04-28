package fr.delphes.twitch.api.channelPoll

import fr.delphes.twitch.api.channelPoll.payload.ChannelPollBeginCondition
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollBeginPayload
import fr.delphes.twitch.api.channelPoll.payload.SubscribeChannelPollBegin
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelPollBeginEventSubConfiguration :
    EventSubConfiguration<ChannelPollBeginPayload, ChannelPollBeginCondition>(
        EventSubTopic.CHANNEL_POLL_BEGIN
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelPollBegin(
        ChannelPollBeginCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPollBeginPayload, ChannelPollBeginCondition> {
        return call.receive()
    }
}