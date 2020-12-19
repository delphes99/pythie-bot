package fr.delphes

import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.application.Application

class TwitchConnector : Connector {
    override fun endpoints(application: Application) {
        //TODO
    }

    override fun connect() {
        //TODO
    }

    override suspend fun execute(event: OutgoingEvent) {

    }
}