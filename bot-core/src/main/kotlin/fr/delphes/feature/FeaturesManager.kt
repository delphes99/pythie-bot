package fr.delphes.feature

import fr.delphes.bot.Bot
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.descriptor.Descriptor
import fr.delphes.descriptor.registry.DescriptorRegistry
import fr.delphes.descriptor.registry.ToDescriptorMapper
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import fr.delphes.rework.feature.FeatureRuntime
import fr.delphes.state.StateManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FeaturesManager(
    private val repository: ExperimentalFeatureConfigurationRepository,
    private val registry: DescriptorRegistry,
    private val globalRegistry: ToDescriptorMapper,
    private val stateManager: StateManager = StateManager(),
    private val customFeatures: List<FeatureDefinition> = emptyList()
) {
    private val runtimes = customFeatures.associateWith { definition ->
        definition.buildRuntime(stateManager)
    }

    suspend fun getDescriptors(): List<Descriptor> {
        return repository.getAll().mapNotNull { registry.descriptorOf(it, globalRegistry) }
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
}