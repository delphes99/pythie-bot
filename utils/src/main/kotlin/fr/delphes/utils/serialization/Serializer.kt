package fr.delphes.utils.serialization

import kotlinx.serialization.json.Json

val Serializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
}