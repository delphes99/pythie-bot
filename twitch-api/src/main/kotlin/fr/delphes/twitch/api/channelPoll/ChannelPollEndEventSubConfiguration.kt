package fr.delphes.twitch.api.channelPoll

import fr.delphes.twitch.api.channelPoll.payload.ChannelPollEndCondition
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollEndPayload
import fr.delphes.twitch.api.channelPoll.payload.SubscribeChannelPollEnd
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelPollEndEventSubConfiguration :
    EventSubConfiguration<ChannelPollEndPayload, ChannelPollEndCondition>(
        EventSubTopic.CHANNEL_POLL_END
    ) {
    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelPollEnd(
        ChannelPollEndCondition(
            userId
        ),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelPollEndPayload, ChannelPollEndCondition> {
        return call.receive()
    }
}