package fr.delphes.bot.twitch.adapter

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.webserver.payload.newFollow.NewFollowPayload
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import kotlinx.coroutines.runBlocking

class NewFollowHandler : TwitchIncomingEventHandler<ApplicationRequest> {
    override fun transform(twitchEvent: ApplicationRequest): List<IncomingEvent> {
        val payload = runBlocking {
            twitchEvent.call.receive<NewFollowPayload>()
        }
        return payload.data.map { d -> NewFollow(d) }
    }
}