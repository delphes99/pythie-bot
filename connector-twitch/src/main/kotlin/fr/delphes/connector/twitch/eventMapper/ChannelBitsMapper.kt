package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelCheer.NewCheer

class ChannelBitsMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<NewCheer> {
    override suspend fun handle(
        twitchEvent: NewCheer
    ): List<TwitchIncomingEvent> {
        connector.whenRunning {
            clientBot.channelOf(twitchEvent.channel)?.state?.newCheer(twitchEvent)
        }

        return listOf(
            BitCheered(
                twitchEvent.channel,
                twitchEvent.cheerer,
                twitchEvent.bits,
                twitchEvent.message
            )
        )
    }
}