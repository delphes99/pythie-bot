package fr.delphes.feature

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FeatureConfigurationRepository(
    path: String,
    serializer: Json,
) : ExperimentalFeatureConfigurationRepository, FileRepository<List<ExperimentalFeatureConfiguration<out ExperimentalFeature<ExperimentalFeatureRuntime>>>> (
    path,
    serializer = { serializer.encodeToString(it) },
    deserializer = { serializer.decodeFromString(it) },
    initializer = { emptyList() }
) {
    override suspend fun getAll(): List<ExperimentalFeatureConfiguration<out ExperimentalFeature<*>>> {
        return load()
    }
}