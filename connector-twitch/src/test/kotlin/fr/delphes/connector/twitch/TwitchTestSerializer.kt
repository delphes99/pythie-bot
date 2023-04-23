package fr.delphes.connector.twitch

import kotlinx.serialization.json.Json

val twitchTestSerializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
    //TODO centralize serialization module
    serializersModule = twitchSerializersModule
}