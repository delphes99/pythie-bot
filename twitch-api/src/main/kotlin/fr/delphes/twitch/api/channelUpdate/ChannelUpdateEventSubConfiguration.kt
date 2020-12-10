package fr.delphes.twitch.api.channelUpdate

import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateCondition
import fr.delphes.twitch.api.channelUpdate.payload.ChannelUpdateEventPayload
import fr.delphes.twitch.api.channelUpdate.payload.SubscribeChannelUpdate
import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import fr.delphes.twitch.eventSub.EventSubConfiguration
import fr.delphes.twitch.eventSub.payload.notification.NotificationPayload
import fr.delphes.twitch.eventSub.payload.subscription.SubscribeTransport
import io.ktor.application.ApplicationCall
import io.ktor.request.receive

class ChannelUpdateEventSubConfiguration(
    listener: (ChannelUpdate) -> Unit
) : EventSubConfiguration<ChannelUpdate, ChannelUpdateEventPayload, ChannelUpdateCondition>(
    "channelUpdate",
    listener
) {
    override fun transform(
        payload: ChannelUpdateEventPayload
    ): ChannelUpdate {
        return ChannelUpdate(payload.title, payload.language, Game(GameId(payload.category_id), payload.category_name))
    }

    override fun subscribePayload(
        userId: String,
        transport: SubscribeTransport
    ): SubscribeChannelUpdate {
        return SubscribeChannelUpdate(
            ChannelUpdateCondition(userId),
            transport
        )
    }

    override suspend fun parse(call: ApplicationCall): NotificationPayload<ChannelUpdateEventPayload, ChannelUpdateCondition> {
        return call.receive()
    }
}