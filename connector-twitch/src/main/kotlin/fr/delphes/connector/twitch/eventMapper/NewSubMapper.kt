package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.channelSubscribe.NewSub as NewSubTwitch

class NewSubMapper(
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<NewSubTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.channelSubscribe.NewSub
    ): List<TwitchIncomingEvent> {
        //TODO move to connector implementation
        connector.whenRunning {
            clientBot.channelOf(twitchEvent.channel)?.state?.newSub(twitchEvent.user)
        }

        return listOf(NewSub(twitchEvent.channel, twitchEvent.user))
    }
}