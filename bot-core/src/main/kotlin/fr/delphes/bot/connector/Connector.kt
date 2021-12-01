package fr.delphes.bot.connector

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.utils.store.Store
import io.ktor.application.Application

interface Connector {
    val configFilepath: String

    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    suspend fun connect()

    suspend fun execute(event: OutgoingEvent)
}