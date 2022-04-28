package fr.delphes.twitch

import fr.delphes.twitch.api.user.payload.UserInfosForToken
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.utils.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json

class TwitchHelixClient : TwitchHelixApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Serializer)
        }
    }

    override suspend fun getUserInfosOf(authToken: AuthToken): UserInfosForToken {
        return httpClient.get("https://id.twitch.tv/oauth2/userinfo") {
            header("Authorization", "Bearer ${authToken.access_token}")
        }.body()
    }
}