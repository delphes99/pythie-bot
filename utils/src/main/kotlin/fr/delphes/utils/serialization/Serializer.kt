package fr.delphes.utils.serialization

import kotlinx.serialization.json.Json

//TODO replacement with bot instance
val Serializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
}