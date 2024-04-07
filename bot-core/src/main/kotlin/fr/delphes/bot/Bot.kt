package fr.delphes.bot

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.bot.connector.ConnectorType
import fr.delphes.bot.event.EventsManager
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.monitoring.StatisticService
import fr.delphes.bot.overlay.OverlayRepository
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.feature.FeatureConfigurationBuilderRegistry
import fr.delphes.feature.FeatureConfigurationRepository
import fr.delphes.feature.FeaturesManager
import fr.delphes.feature.NonEditableFeature
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.SerializersModule

class Bot(
    val configuration: BotConfiguration,
    @Deprecated("Use featuresManager instead")
    val legacyfeatures: List<NonEditableFeature>,
    val features: List<FeatureDefinition>,
    val featureConfigurationsType: List<FeatureConfigurationBuilderRegistry>,
    private val connectorInitializers: List<ConnectorInitializer>,
    featuresDynamicFormRegistry: DynamicFormRegistry,
    featureSerializersModule: SerializersModule,
    val clock: Clock = SystemClock,
) : IncomingEventHandler,
    OutgoingEventProcessor {

    companion object {
        //TODO split bot responsability to inject eventHandling so we can inject that in the connector build in init
        fun launchBot(
            configuration: BotConfiguration,
            connectors: List<ConnectorInitializer>,
            nonEditableFeatures: List<NonEditableFeature>,
            featureDefinitions: List<FeatureDefinition>,
            featureConfigurationBuilderRegistries: List<FeatureConfigurationBuilderRegistry>,
            featuresDynamicFormRegistry: DynamicFormRegistry,
            featureSerializerModule: SerializersModule,
        ) {
            val bot = Bot(
                configuration,
                nonEditableFeatures,
                featureDefinitions,
                featureConfigurationBuilderRegistries,
                connectors,
                //TODO features : connector initializer ?
                featuresDynamicFormRegistry,
                featureSerializerModule
            )

            bot.init()
        }
    }

    val serializer = buildSerializer(featureSerializersModule, connectorInitializers)

    val stateManager = StateManager()
        .withState(ClockState())

    val enumerationStates get() = connectors.flatMap(Connector<*, *>::enumerationStates)

    val featuresManager = buildFeatureManager()

    val statisticService = StatisticService(configuration, serializer)

    val connectors = connectorInitializers
        .map { connector -> connector.buildConnector(this) }

    val dynamicFormRegistry = DynamicFormRegistry.compose(
        *connectorInitializers.map { it.dynamicFormRegistry }.toTypedArray(),
        featuresDynamicFormRegistry
    )

    internal val overlayRepository =
        OverlayRepository(configuration.pathOf("overlays", "overlays.json"))

    override suspend fun handle(incomingEvent: IncomingEventWrapper<out IncomingEvent>) {
        listOf(legacyfeatures)
            .flatten()
            .flatMap { feature -> feature.handleIncomingEvent(incomingEvent.data, this) }
            .forEach { event -> processOutgoingEvent(event) }
        statisticService.handle(incomingEvent)
        connectors.forEach { it.handleForInternal(incomingEvent) }
        featuresManager.handle(incomingEvent, this)
    }

    val eventsManager = EventsManager(connectorInitializers)

    override suspend fun processOutgoingEvent(event: OutgoingEvent) {
        connectors.forEach { connector ->
            connector.execute(event)
        }
    }

    fun init() {
        WebServer(
            bot = this,
            internalModules = connectors.map { connector -> { application -> connector.internalEndpoints(application) } },
            publicModules = connectors.map { connector -> { application -> connector.publicEndpoints(application) } }
        )

        // After initial state
        runBlocking {
            connectors.map { connector -> async { connector.connect() } }.awaitAll()
            connectors.flatMap(Connector<*, *>::states).forEach { state ->
                stateManager.put(state)
            }

            handle(IncomingEventWrapper(BotStarted, clock.now()))
        }
    }

    inline fun <reified T : Connector<*, *>> connector(): T? {
        return connectors.filterIsInstance<T>().firstOrNull()
    }

    fun findConnector(name: ConnectorType): Connector<*, *>? = connectors
        .firstOrNull { connector -> connector.connectorType == name }


    private fun buildFeatureManager(): FeaturesManager {
        val featureConfigurationRepository = FeatureConfigurationRepository(
            configuration.pathOf("features"),
            serializer
        )
        return FeaturesManager(stateManager, features, featureConfigurationRepository)
    }
}