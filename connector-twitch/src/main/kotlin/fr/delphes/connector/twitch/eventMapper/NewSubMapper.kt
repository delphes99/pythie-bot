package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelSubscribe.NewSub as NewSubTwitch

class NewSubMapper(
    private val bot: ClientBot
) : TwitchIncomingEventMapper<NewSubTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelSubscribe.NewSub
    ): List<TwitchIncomingEvent> {
        bot.channelOf(twitchEvent.channel)?.state?.newSub(twitchEvent.user)

        return listOf(NewSub(twitchEvent.channel, twitchEvent.user))
    }
}