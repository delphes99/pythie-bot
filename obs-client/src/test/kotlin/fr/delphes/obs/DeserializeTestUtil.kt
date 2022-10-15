package fr.delphes.obs

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

inline fun <reified T> String.deserialize(): T {
    val json = loadResourceAsText(this)!!
    return serializer.decodeFromString(json)
}