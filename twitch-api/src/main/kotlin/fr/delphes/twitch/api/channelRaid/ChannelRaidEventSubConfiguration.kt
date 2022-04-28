package fr.delphes.twitch.api.channelRaid

import fr.delphes.twitch.api.channelRaid.payload.ChannelRaidCondition
import fr.delphes.twitch.api.channelRaid.payload.ChannelRaidPayload
import fr.delphes.twitch.api.channelRaid.payload.SubscribeChannelRaid
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.EventSubTopic
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive

class ChannelRaidEventSubConfiguration :
    EventSubConfiguration<ChannelRaidPayload, ChannelRaidCondition>(
        EventSubTopic.INCOMING_RAID
    ) {

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ) = SubscribeChannelRaid(
        ChannelRaidCondition(to_broadcaster_user_id = userId),
        transport
    )

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelRaidPayload, ChannelRaidCondition> {
        return call.receive()
    }
}