package fr.delphes.test.serialization

import fr.delphes.utils.loadResourceAsText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@PublishedApi
internal val serializer = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
}

inline fun <reified T> String.readAndDeserialize(): T {
    val json = loadResourceAsText(this)!!
    return serializer.decodeFromString(json)
}

inline fun <reified T> String.deserialize(): T {
    return serializer.decodeFromString(this)
}