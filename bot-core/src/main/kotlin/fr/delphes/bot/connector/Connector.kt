package fr.delphes.bot.connector

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.application.Application

interface Connector {
    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    fun connect(bot: ClientBot)

    suspend fun execute(event: OutgoingEvent)
}