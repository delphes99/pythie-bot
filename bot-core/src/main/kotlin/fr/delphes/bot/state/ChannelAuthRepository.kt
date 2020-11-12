package fr.delphes.bot.state

import fr.delphes.bot.util.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChannelAuthRepository(
    path: String
) : FileRepository<ChannelAuth>(
    path,
    serializer = { Json.encodeToString(it) },
    deserializer = { Json.decodeFromString(it) },
    initializer = { ChannelAuth.empty () }
)