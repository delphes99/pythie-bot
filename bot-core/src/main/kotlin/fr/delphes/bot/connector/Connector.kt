package fr.delphes.bot.connector

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.utils.store.StateManager
import io.ktor.application.Application

interface Connector {
    val configFilepath: String

    val states: List<StateManager<*>>

    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    suspend fun connect()

    suspend fun execute(event: OutgoingEvent)
}