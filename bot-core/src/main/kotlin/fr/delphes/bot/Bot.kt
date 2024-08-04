package fr.delphes.bot

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.bot.connector.ConnectorType
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.media.MediasService
import fr.delphes.bot.monitoring.StatisticService
import fr.delphes.bot.overlay.OverlayRepository
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.feature.FeaturesManager
import fr.delphes.feature.FileFeatureConfigurationRepository
import fr.delphes.feature.NonEditableFeature
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.SerializersModule

class Bot(
    val configuration: BotConfiguration,
    @Deprecated("Use featuresManager instead")
    val legacyfeatures: List<NonEditableFeature>,
    val features: List<FeatureDefinition>,
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
            featuresDynamicFormRegistry: DynamicFormRegistry,
            featureSerializerModule: SerializersModule,
        ) {
            val bot = Bot(
                configuration,
                nonEditableFeatures,
                featureDefinitions,
                connectors,
                //TODO features : connector initializer ?
                featuresDynamicFormRegistry,
                featureSerializerModule,
            )

            bot.init()
        }
    }

    val mediaService: MediasService = MediasService(configuration)

    val serializer = buildSerializer(featureSerializersModule, connectorInitializers)

    val stateManager = StateManager()
        .withState(ClockState())

    val enumerationStates get() = connectors.flatMap(Connector<*, *>::enumerationStates)

    val statisticService = StatisticService(configuration, serializer)

    val connectors = connectorInitializers
        .map { connector -> connector.buildConnector(this) }

    val dynamicFormRegistry = DynamicFormRegistry.compose(
        *connectorInitializers.map { it.dynamicFormRegistry }.toTypedArray(),
        featuresDynamicFormRegistry
    )

    val featuresManager = buildFeatureManager(dynamicFormRegistry)

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
            BotInitializer(stateManager, connectors).initialize()

            handle(IncomingEventWrapper(BotStarted, clock.now()))
        }
    }

    inline fun <reified T : Connector<*, *>> connector(): T? {
        return connectors.filterIsInstance<T>().firstOrNull()
    }

    fun findConnector(name: ConnectorType): Connector<*, *>? = connectors
        .firstOrNull { connector -> connector.connectorType == name }


    private fun buildFeatureManager(formRegistry: DynamicFormRegistry): FeaturesManager {
        val featureConfigurationRepository = FileFeatureConfigurationRepository(
            configuration.pathOf("features"),
            serializer,
            formRegistry
        )
        return FeaturesManager(stateManager, features, featureConfigurationRepository)
    }
}