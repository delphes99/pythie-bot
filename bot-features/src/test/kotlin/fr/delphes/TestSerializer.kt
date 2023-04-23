package fr.delphes

import fr.delphes.connector.twitch.twitchSerializersModule
import fr.delphes.features.featureSerializersModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val testSerializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
    //TODO centralize serialization module
    serializersModule = SerializersModule {
        include(featureSerializersModule)
        include(twitchSerializersModule)
    }
}