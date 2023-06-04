package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.StateManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FeaturesManager(
    val stateManager: StateManager,
    private val customFeatures: List<FeatureDefinition> = emptyList(),
    private val featureConfigurationRepository: FeatureConfigurationRepository,
) {
    private val compiledRuntimes = customFeatures.associateWith { definition ->
        registerSpecificStatesAndBuildRuntimeOf(definition)
    }

    private var configurableRunTimes = emptyMap<FeatureDefinition, FeatureRuntime>()

    init {
        loadConfigurableFeatures()
    }

    fun loadConfigurableFeatures() {
        runBlocking {
            configurableRunTimes = featureConfigurationRepository.load()
                .map { configuration ->
                    configuration.buildFeature()
                }.associateWith { definition ->
                    registerSpecificStatesAndBuildRuntimeOf(definition)
                }
        }
    }

    private fun registerSpecificStatesAndBuildRuntimeOf(definition: FeatureDefinition): FeatureRuntime {
        //TODO unsubscribe old specific states
        definition.getSpecificStates(stateManager.readOnly).forEach {
            stateManager.put(it)
        }
        return definition.buildRuntime(stateManager.readOnly)
    }

    suspend fun handle(event: IncomingEventWrapper<out IncomingEvent>, bot: Bot) {
        val runtimes = compiledRuntimes.values + configurableRunTimes.values
        coroutineScope {
            runtimes.forEach { runtime ->
                launch {
                    runtime.handleIncomingEvent(event, bot)
                }
            }
        }
    }

    val featureDefinitions: List<FeatureDefinition> get() = customFeatures + configurableRunTimes.keys

    fun getRuntime(id: FeatureId): FeatureRuntime? {
        return compiledRuntimes.filterKeys { it.id == id }.values.firstOrNull()
    }

    suspend fun getEditableFeatures(): List<FeatureConfiguration> {
        return featureConfigurationRepository.load()
    }

    suspend fun getEditableFeature(id: FeatureId): FeatureConfiguration? {
        return getEditableFeatures().find { it.id == id }
    }

    suspend fun upsertFeature(feature: FeatureConfiguration) {
        featureConfigurationRepository.upsert(feature)
        loadConfigurableFeatures()
    }
}