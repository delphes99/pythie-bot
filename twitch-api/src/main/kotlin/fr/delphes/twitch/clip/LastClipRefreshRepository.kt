package fr.delphes.twitch.clip

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LastClipRefreshRepository(
    path: String,
    initializer: suspend () -> LastClipRefresh
) : FileRepository<LastClipRefresh>(
    path,
    serializer = { Json.encodeToString(it) },
    deserializer = { Json.decodeFromString(it) },
    initializer = initializer
)