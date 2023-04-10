package fr.delphes.bot

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.CoreOutgoingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.Pause
import fr.delphes.bot.event.outgoing.PlaySound
import fr.delphes.bot.overlay.OverlayRepository
import fr.delphes.feature.FeatureConfigurationRepository
import fr.delphes.feature.FeaturesManager
import fr.delphes.feature.NonEditableFeature
import fr.delphes.rework.feature.CustomFeature
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import fr.delphes.utils.exhaustive
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Bot(
    val configuration: BotConfiguration,
    @Deprecated("Use featuresManager instead")
    val legacyfeatures: List<NonEditableFeature>,
    val serializer: Json,
    val features: List<CustomFeature>,
) : IncomingEventHandler, OutgoingEventProcessor {
    val featuresManager = buildFeatureManager()

    private val _connectors = mutableListOf<Connector<*, *>>()
    val connectors get(): List<Connector<*, *>> = _connectors

    internal val overlayRepository =
        OverlayRepository(configuration.pathOf("overlays", "overlays.json"))

    val alerts = Channel<Alert>()

    override suspend fun handle(incomingEvent: IncomingEvent) {
        listOf(legacyfeatures)
            .flatten()
            .flatMap { feature -> feature.handleIncomingEvent(incomingEvent, this) }
            .forEach { event -> processOutgoingEvent(event) }

        featuresManager.handle(incomingEvent, this)
    }

    override suspend fun processOutgoingEvent(event: OutgoingEvent) {
        if (event is CoreOutgoingEvent) {
            when (event) {
                is Alert -> alerts.send(event)
                is Pause -> delay(event.delay.toMillis())
                is PlaySound -> alerts.send(
                    Alert(
                        "playSound",
                        "mediaName" to event.mediaName,
                    )
                ) // TODO move appart from alert
            }.exhaustive()
        } else {
            _connectors.forEach { connector ->
                connector.execute(event)
            }
        }
    }

    fun init(vararg connectorsToAdd: Connector<*, *>) {
        _connectors.addAll(connectorsToAdd)

        WebServer(
            bot = this,
            internalModules = _connectors.map { connector -> { application -> connector.internalEndpoints(application) } },
            publicModules = _connectors.map { connector -> { application -> connector.publicEndpoints(application) } }
        )

        // After initial state
        runBlocking {
            _connectors.map { connector -> async { connector.connect() } }.awaitAll()
            _connectors.flatMap(Connector<*, *>::states).forEach { state ->
                featuresManager.stateManager.put(state)
            }

            handle(BotStarted)
        }
    }

    inline fun <reified T : Connector<*, *>> connector(): T? {
        return connectors.filterIsInstance<T>().firstOrNull()
    }

    fun findConnector(name: String?): Connector<*, *>? = connectors
        .firstOrNull { connector -> connector.connectorName == name }


    private fun buildFeatureManager(): FeaturesManager {
        val stateManager = StateManager()
            .withState(ClockState())

        val featureConfigurationRepository = FeatureConfigurationRepository(
            configuration.pathOf("features"),
            serializer
        )
        return FeaturesManager(stateManager, features, featureConfigurationRepository)
    }

}