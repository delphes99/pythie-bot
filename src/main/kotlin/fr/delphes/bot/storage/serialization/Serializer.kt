package fr.delphes.bot.storage.serialization

import kotlinx.serialization.json.Json

val Serializer = Json {
    ignoreUnknownKeys = true;
    isLenient = false;
}