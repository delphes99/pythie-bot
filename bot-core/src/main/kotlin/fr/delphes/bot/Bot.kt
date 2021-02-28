package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.feature.Feature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class Bot(
    val publicUrl: String,
    val configFilepath: String,
    val connectors: List<Connector>,
    val features: List<Feature>
) {
    val alerts = Channel<Alert>()
    private val eventHandlers = EventHandlers()

    suspend fun handleIncomingEvent(incomingEvent: IncomingEvent) {
        eventHandlers.handleEvent(incomingEvent, this).forEach { event ->
            connectors.forEach { connector ->
                connector.execute(event)
            }
        }
    }

    fun init() {
        features.forEach { feature ->
            feature.registerHandlers(eventHandlers)
        }

        //TODO breack interdependency
        connectors.forEach { it.init(this) }

        WebServer(
            bot = this,
            internalModules = connectors.map { connector -> { application -> connector.internalEndpoints(application) } },
            publicModules = connectors.map { connector -> { application -> connector.publicEndpoints(application) } }
        )

        // After initial state
        connectors.forEach { it.connect() }

        //TODO move to connector
        runBlocking {
            connectors.forEach { it.resetWebhook() }
        }
    }

    inline fun <reified T: Connector> connector(): T? {
        return connectors.filterIsInstance<T>().firstOrNull()
    }
}