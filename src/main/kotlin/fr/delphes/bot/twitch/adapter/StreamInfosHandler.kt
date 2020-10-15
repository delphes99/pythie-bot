package fr.delphes.bot.twitch.adapter

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.StreamOffline
import fr.delphes.bot.event.incoming.StreamOnline
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.webserver.payload.streamInfos.StreamInfosPayload
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import kotlinx.coroutines.runBlocking

class StreamInfosHandler : TwitchIncomingEventHandler<ApplicationRequest> {
    override fun handle(twitchEvent: ApplicationRequest, channel: ChannelInfo): List<IncomingEvent> {
        val payload = runBlocking {
            twitchEvent.call.receive<StreamInfosPayload>()
        }
        val streamInfos = payload.data

        return if (streamInfos.isEmpty()) {
            listOf(StreamOffline())
        } else {
            streamInfos.map{ StreamOnline() }
        }
    }
}