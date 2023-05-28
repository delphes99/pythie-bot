package fr.delphes.features.twitch.voth

import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class FileVOTHStateRepository(filePath: String) : FileRepository<VOTHStateData>(
    filePath = filePath,
    initializer = { VOTHStateData() },
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) }
)