package fr.delphes.bot.util.http

import fr.delphes.bot.util.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

val httpClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(Serializer)
    }
}