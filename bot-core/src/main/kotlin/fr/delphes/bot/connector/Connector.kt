package fr.delphes.bot.connector

import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.application.Application

interface Connector {
    fun endpoints(application: Application)

    fun connect()

    suspend fun execute(event: OutgoingEvent)
}