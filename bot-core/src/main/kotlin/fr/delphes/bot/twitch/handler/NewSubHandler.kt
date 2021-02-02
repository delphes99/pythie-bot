package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.channelSubscribe.NewSub as NewSubTwitch

class NewSubHandler(
    private val bot: ClientBot
) : TwitchIncomingEventHandler<NewSubTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelSubscribe.NewSub
    ): List<TwitchIncomingEvent> {
        bot.channelOf(twitchEvent.channel)?.state?.newSub(twitchEvent.user)

        return listOf(NewSub(twitchEvent.channel, twitchEvent.user))
    }
}