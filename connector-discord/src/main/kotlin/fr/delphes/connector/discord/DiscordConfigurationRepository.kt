package fr.delphes.connector.discord

import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class DiscordConfigurationRepository(
    path: String
): FileRepository<DiscordConfiguration>(
    path,
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) },
    initializer = { DiscordConfiguration.empty() }
)