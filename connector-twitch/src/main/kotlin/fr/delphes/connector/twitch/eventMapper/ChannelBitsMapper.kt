package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelCheer.NewCheer
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerEventPayload
import fr.delphes.twitch.api.user.User

class ChannelBitsMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<ChannelCheerEventPayload> {
    override suspend fun handle(
        twitchEvent: ChannelCheerEventPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        val newCheer = NewCheer(
            channel,
            twitchEvent.user_name?.let(::User),
            twitchEvent.bits,
            twitchEvent.message
        )

        //TODO use bot model : BitCheered
        connector.whenRunning {
            clientBot.channelOf(channel)?.state?.newCheer(newCheer)
        }

        return listOf(
            BitCheered(
                newCheer.channel,
                newCheer.cheerer,
                newCheer.bits,
                newCheer.message
            )
        )
    }
}