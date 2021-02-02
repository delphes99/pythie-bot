package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.api.streamOffline.StreamOffline as StreamOfflineTwitch

class StreamOfflineHandler(
    private val bot: ClientBot
) : TwitchIncomingEventHandler<StreamOfflineTwitch> {
    override suspend fun handle(
        twitchEvent: fr.delphes.twitch.api.streamOffline.StreamOffline
    ): List<TwitchIncomingEvent> {
        bot.channelOf(twitchEvent.channel)?.state?.changeCurrentStream(null)

        return listOf(StreamOffline(twitchEvent.channel))
    }
}