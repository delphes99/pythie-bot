package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.StateManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FeaturesManager(
    val stateManager: StateManager,
    private val customFeatures: List<FeatureDefinition> = emptyList(),
    private val featureConfigurationRepository: FeatureConfigurationRepository
) {
    private val runtimes = customFeatures.associateWith { definition ->
        definition.buildRuntime(stateManager)
    }

    suspend fun handle(event: IncomingEvent, bot: Bot) {
        coroutineScope {
            runtimes.values.forEach { runtime ->
                launch {
                    runtime.handleIncomingEvent(event, bot)
                }
            }
        }
    }

    val featureDefinitions: List<FeatureDefinition> get() = customFeatures

    fun getRuntime(id: FeatureId): FeatureRuntime? {
        return runtimes.filterKeys { it.id == id }.values.firstOrNull()
    }

    suspend fun getEditableFeature(): List<FeatureConfiguration> {
        return featureConfigurationRepository.load()
    }
}