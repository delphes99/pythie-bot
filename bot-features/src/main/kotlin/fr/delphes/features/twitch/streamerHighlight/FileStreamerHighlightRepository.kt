package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.feature.FileStateRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class FileStreamerHighlightRepository(filePath: String) : FileStateRepository<StreamerHighlightState>(
    filePath = filePath,
    initializer = { StreamerHighlightState() },
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) }
)