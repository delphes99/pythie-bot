package fr.delphes.features.twitch.voth

import fr.delphes.feature.FileStateRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class FileVOTHStateRepository(filePath: String) : FileStateRepository<VOTHState>(
    filePath = filePath,
    initializer =  { VOTHState() },
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) }
)