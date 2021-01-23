package fr.delphes

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TwitchConfigurationRepository(
    filePath: String
) : FileRepository<TwitchConfiguration>(
    filePath = filePath,
    serializer = { Json.encodeToString(it) },
    deserializer = { Json.decodeFromString(it) },
    initializer = { TwitchConfiguration.empty }
)