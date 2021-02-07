package fr.delphes.connector.twitch.eventHandler

import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelCheer.NewCheer

class ChannelBitsHandler(
    private val bot: ClientBot
) : TwitchIncomingEventHandler<NewCheer> {
    override suspend fun handle(
        twitchEvent: NewCheer
    ): List<TwitchIncomingEvent> {
        bot.channelOf(twitchEvent.channel)?.state?.newCheer(twitchEvent)

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