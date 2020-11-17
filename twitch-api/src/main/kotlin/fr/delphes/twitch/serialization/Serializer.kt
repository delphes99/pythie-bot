package fr.delphes.twitch.serialization

import kotlinx.serialization.json.Json

val Serializer = Json {
    ignoreUnknownKeys = true;
    isLenient = false;
}