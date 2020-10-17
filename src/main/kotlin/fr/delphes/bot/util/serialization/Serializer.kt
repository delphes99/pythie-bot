package fr.delphes.bot.util.serialization

import kotlinx.serialization.json.Json

val Serializer = Json {
    ignoreUnknownKeys = true;
    isLenient = false;
}