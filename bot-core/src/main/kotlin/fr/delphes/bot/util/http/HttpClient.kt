package fr.delphes.bot.util.http

import fr.delphes.utils.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Serializer)
    }
}