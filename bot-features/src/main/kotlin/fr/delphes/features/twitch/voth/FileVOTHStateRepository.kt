package fr.delphes.features.twitch.voth

import fr.delphes.feature.FileStateRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FileVOTHStateRepository(filePath: String) : FileStateRepository<VOTHState>(
    filePath = filePath,
    initializer =  { VOTHState() },
    serializer = { Json.encodeToString(it) },
    deserializer = { Json.decodeFromString(it) }
)