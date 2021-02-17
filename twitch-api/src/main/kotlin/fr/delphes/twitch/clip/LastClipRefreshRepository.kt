package fr.delphes.twitch.clip

import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class LastClipRefreshRepository(
    path: String,
    initializer: suspend () -> LastClipRefresh
) : FileRepository<LastClipRefresh>(
    path,
    serializer = { Serializer.encodeToString(it) },
    deserializer = { Serializer.decodeFromString(it) },
    initializer = initializer
)