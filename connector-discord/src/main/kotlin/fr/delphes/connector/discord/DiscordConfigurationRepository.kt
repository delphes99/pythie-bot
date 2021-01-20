package fr.delphes.connector.discord

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DiscordConfigurationRepository(
    path: String
): FileRepository<DiscordConfiguration>(
    path,
    serializer = { Json.encodeToString(it) },
    deserializer = { Json.decodeFromString(it) },
    initializer = { DiscordConfiguration.empty() }
)