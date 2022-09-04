package fr.delphes.feature

import mu.KotlinLogging

class FeaturePlatform(
    private val repository: FeaturesProvider
) {
    var runTimes: List<ExperimentalFeatureRuntime> = emptyList()

    suspend fun load() {
        runTimes = repository
            .get()
            .mapNotNull {
                try {
                    it.buildRuntime()
                } catch (e: Exception) {
                    LOGGER.error(e) { "Error when building runtime [${it.id}] : ${e.message}" }
                    null
                }
            }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}