package fr.delphes.bot.connector

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEvent
import io.ktor.application.Application

interface Connector {
    val configFilepath: String

    fun internalEndpoints(application: Application)

    fun publicEndpoints(application: Application)

    fun init(bot: Bot)

    fun connect()

    suspend fun execute(event: OutgoingEvent)

    suspend fun resetWebhook()
}