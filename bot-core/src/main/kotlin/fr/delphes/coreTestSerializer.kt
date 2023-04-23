package fr.delphes

import fr.delphes.bot.coreSerializersModule
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val serializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
    serializersModule = SerializersModule {
        include(coreSerializersModule)
    }
}