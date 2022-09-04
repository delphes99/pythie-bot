package fr.delphes.feature

import mu.KotlinLogging

class FromConfigurationsFeatureProvider(
    private val configurationRepository: ExperimentalFeatureConfigurationRepository
) : FeaturesProvider {
    override suspend fun get(): List<ExperimentalFeature<ExperimentalFeatureRuntime>> {
        return configurationRepository.getAll().mapNotNull {
            try {
                it.buildFeature()
            } catch (e: Exception) {
                LOGGER.error(e) { "Error while loading editable feature [${it.id}] : ${e.message}" }
                null
            }
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}