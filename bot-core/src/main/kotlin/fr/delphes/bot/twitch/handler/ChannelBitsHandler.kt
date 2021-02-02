package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.BitCheered
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
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