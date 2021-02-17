package fr.delphes.twitch.auth

import fr.delphes.utils.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.parameter
import io.ktor.client.request.post

class AuthClient : AuthApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
    }

    private suspend inline fun <reified T> String.post(vararg parameters: Pair<String, String>): T {
        return httpClient.post(this) {
            parameters.forEach { parameter ->
                parameter(parameter.first, parameter.second)
            }
        }
    }

    override suspend fun refreshToken(oldToken: AuthToken, clientId: String, clientSecret: String): AuthToken {
        return "https://id.twitch.tv/oauth2/token".post(
            "grant_type" to "refresh_token",
            "refresh_token" to oldToken.refresh_token!!,
            "client_id" to clientId,
            "client_secret" to clientSecret
        )
    }

    override suspend fun getAppToken(clientId: String, clientSecret: String): AuthToken {
        return "https://id.twitch.tv/oauth2/token".post(
            "grant_type" to "client_credentials",
            "scope" to "bits:read channel:read:hype_train channel:read:subscriptions chat:read channel:moderate channel:read:redemptions",
            "client_id" to clientId,
            "client_secret" to clientSecret
        )
    }
}