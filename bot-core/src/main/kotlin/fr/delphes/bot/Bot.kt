package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.feature.EditableFeature
import fr.delphes.feature.Feature
import fr.delphes.feature.NonEditableFeature
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

class Bot(
    val publicUrl: String,
    val configFilepath: String,
    val features: List<NonEditableFeature<*>>,
    val editableFeatures: List<EditableFeature<*>>, //TODO move to a repository
    val featureSerializationModule: SerializersModule
) {
    private val _connectors = mutableListOf<Connector>()
    val connectors get(): List<Connector> = _connectors

    val alerts = Channel<Alert>()
    private val eventHandlers = EventHandlers()

    val serializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
        encodeDefaults = true
        coerceInputValues = true
        serializersModule = featureSerializationModule
    }

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