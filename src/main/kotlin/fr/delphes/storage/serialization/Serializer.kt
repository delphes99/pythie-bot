package fr.delphes.storage.serialization

import kotlinx.serialization.json.Json

val Serializer = Json {
    ignoreUnknownKeys = true;
    isLenient = false;
}