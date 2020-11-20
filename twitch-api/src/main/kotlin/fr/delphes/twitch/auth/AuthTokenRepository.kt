package fr.delphes.twitch.auth

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.IllegalStateException

class AuthTokenRepository(
    path: String,
    getToken: suspend () -> AuthToken = { throw IllegalStateException("Must provide an access token") }
) : FileRepository<AuthToken>(
    path,
    serializer = { Json.encodeToString(it) },
    deserializer = { Json.decodeFromString(it) },
    initializer = getToken
)