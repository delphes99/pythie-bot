package fr.delphes.bot.twitch.adapter

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.webserver.payload.newSub.NewSubPayload
import io.ktor.request.ApplicationRequest
import io.ktor.request.receive
import kotlinx.coroutines.runBlocking

class NewSubHandler : TwitchIncomingEventHandler<ApplicationRequest> {
    override fun transform(twitchEvent: ApplicationRequest): List<IncomingEvent> {
        val payload = runBlocking {
            twitchEvent.call.receive<NewSubPayload>()
        }
        return payload.data.map { d -> NewSub(d) }
    }
}