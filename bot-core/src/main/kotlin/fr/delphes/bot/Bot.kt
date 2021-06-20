package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.CoreOutgoingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.Pause
import fr.delphes.bot.event.outgoing.PlaySound
import fr.delphes.bot.overlay.OverlayRepository
import fr.delphes.feature.EditableFeature
import fr.delphes.feature.NonEditableFeature
import fr.delphes.utils.exhaustive
import fr.delphes.utils.store.Action
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.File

class Bot(
    val publicUrl: String,
    val configFilepath: String,
    val features: List<NonEditableFeature<*>>,
    val editableFeatures: List<EditableFeature<*>>, //TODO move to a repository
    private val featureSerializationModule: SerializersModule,
) {
    private val _connectors = mutableListOf<Connector>()
    val connectors get(): List<Connector> = _connectors

    internal val overlayRepository = OverlayRepository("${configFilepath}${File.separator}overlays${File.separator}overlays.json")

    val alerts = Channel<Alert>()

    val serializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
        encodeDefaults = true
        coerceInputValues = true
        serializersModule = featureSerializationModule
    }

    suspend fun handleIncomingEvent(incomingEvent: IncomingEvent) {
        listOf(features, editableFeatures)
            .flatten()
            .flatMap { feature -> feature.handleIncomingEvent(incomingEvent, this) }
            .forEach { event -> handleOutgoingEvent(event) }
    }

    private suspend fun handleOutgoingEvent(event: OutgoingEvent) {
        if(event is CoreOutgoingEvent) {
            when(event) {
                is Alert -> alerts.send(event)
                is Pause -> delay(event.delay.toMillis())
                is PlaySound -> alerts.send(Alert("playSound", "mediaName" to event.mediaName)) // TODO move appart from alert
            }.exhaustive()
        } else {
            _connectors.forEach { connector ->
                connector.execute(event)
            }
        }
    }

    fun init(vararg connectorsToAdd: Connector) {
        _connectors.addAll(connectorsToAdd)

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

    fun applyAction(action: Action) {
        connectors
            .flatMap { it.states }
            .forEach { stateManager -> stateManager.handle(action) }
    }
}