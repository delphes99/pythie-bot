package fr.delphes.feature.featureNew

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//TODO remove when features is removed
class OldFeatureConfigurationRepository(
    path: String,
    serializer: Json,
) : FileRepository<List<FeatureConfiguration<out FeatureState>>>(
    path,
    serializer = { serializer.encodeToString(it) },
    deserializer = { serializer.decodeFromString(it) },
    initializer = { emptyList() }
)