package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.feature.Feature
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class Bot(
    val publicUrl: String,
    val configFilepath: String,
    val features: List<Feature>
) {
    private val _connectors = mutableListOf<Connector>()
    val connectors get(): List<Connector> = _connectors

    val alerts = Channel<Alert>()
    private val eventHandlers = EventHandlers()

    suspend fun handleIncomingEvent(incomingEvent: IncomingEvent) {
        eventHandlers.handleEvent(incomingEvent, this).forEach { event ->
            _connectors.forEach { connector ->
                connector.execute(event)
            }
        }
    }

    fun init(vararg connectorsToAdd: Connector) {
        _connectors.addAll(connectorsToAdd)

        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }

        WebServer(
            bot = this,
            internalModules = _connectors.map { connector -> { application -> connector.internalEndpoints(application) } },
            publicModules = _connectors.map { connector -> { application -> connector.publicEndpoints(application) } }
        )

        // After initial state
        runBlocking {
            _connectors.map { connector -> async { connector.connect() } }.awaitAll()
        }
    }

    inline fun <reified T : Connector> connector(): T? {
        return connectors.filterIsInstance<T>().firstOrNull()
    }
}