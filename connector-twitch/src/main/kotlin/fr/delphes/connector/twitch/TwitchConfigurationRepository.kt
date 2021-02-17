package fr.delphes.connector.twitch

import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TwitchConfigurationRepository(
    filePath: String
) : FileRepository<TwitchConfiguration>(
    filePath = filePath,
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) },
    initializer = { TwitchConfiguration.empty }
)