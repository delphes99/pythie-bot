package fr.delphes

import fr.delphes.features.featureSerializersModule
import kotlinx.serialization.json.Json

val testSerializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
    //TODO centralize serialization module
    serializersModule = featureSerializersModule
}