package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.auth.WithAuthToken
import fr.delphes.twitch.payload.games.GetGamesDataPayload
import fr.delphes.twitch.payload.games.GetGamesPayload
import fr.delphes.twitch.payload.streams.StreamInfos
import fr.delphes.twitch.payload.streams.StreamPayload
import fr.delphes.twitch.payload.users.GetUsersDataPayload
import fr.delphes.twitch.payload.users.GetUsersPayload
import fr.delphes.twitch.payload.webhook.GetWebhookSubscriptionsDataPayload
import fr.delphes.twitch.payload.webhook.GetWebhookSubscriptionsPayload
import fr.delphes.twitch.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

internal class HelixClient(
    private val appCredential: TwitchAppCredential,
    private val userCredential: TwitchUserCredential
) : HelixApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
    }

    private suspend inline fun <reified T> String.get(
        twitchUserCredential: WithAuthToken,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(twitchUserCredential) {
            httpClient.get(this) {
                headers(parameters, twitchUserCredential)
            }
        }
    }

    private fun HttpRequestBuilder.headers(
        parameters: Array<out Pair<String, String>>,
        credentials: WithAuthToken
    ) {
        header("Authorization", "Bearer ${credentials.authToken!!.access_token}")
        header("Client-Id", appCredential.clientId)
        parameters.forEach { parameter ->
            parameter(parameter.first, parameter.second)
        }
    }

    private suspend inline fun <reified T> authorizeCall(
        credentials: WithAuthToken,
        doCall: () -> T
    ): T{
        try {
            return doCall()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                credentials.refreshToken()

                return doCall()
            }
            throw e
        }
    }

    override suspend fun getGameByName(name: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>(
            userCredential,
            "name" to name
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getGameById(id: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>(
            userCredential,
            "id" to id
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getUser(userName: String): GetUsersDataPayload? {
        val payload = "https://api.twitch.tv/helix/users".get<GetUsersPayload>(
            userCredential,
            "login" to userName
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getStreamByUserId(userId: String): StreamInfos? {
        val payload = "https://api.twitch.tv/helix/streams".get<StreamPayload>(
            userCredential,
            "user_id" to userId
        )

        return payload.data.firstOrNull()
    }

    override suspend fun getWebhook(): List<GetWebhookSubscriptionsDataPayload> {
        val payload = "https://api.twitch.tv/helix/webhooks/subscriptions".get<GetWebhookSubscriptionsPayload>(
            appCredential
        )

        return payload.data
    }
}