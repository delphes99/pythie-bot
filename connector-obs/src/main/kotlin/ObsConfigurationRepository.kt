package fr.delphes.connector.obs

import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class ObsConfigurationRepository(
    path: String
): FileRepository<ObsConfiguration>(
    path,
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) },
    initializer = { ObsConfiguration.empty() }
)