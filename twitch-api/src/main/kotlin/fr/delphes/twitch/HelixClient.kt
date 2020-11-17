package fr.delphes.twitch

import fr.delphes.twitch.payload.games.GetGamesDataPayload
import fr.delphes.twitch.payload.games.GetGamesPayload
import fr.delphes.twitch.payload.streams.StreamInfos
import fr.delphes.twitch.payload.streams.StreamPayload
import fr.delphes.twitch.payload.users.GetUsersDataPayload
import fr.delphes.twitch.payload.users.GetUsersPayload
import fr.delphes.twitch.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

internal class HelixClient(
    private val clientId: String,
    private val authToken: String
) : HelixApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
    }

    private suspend inline fun <reified T> String.get(vararg parameters: Pair<String, String>): T {
        return httpClient.get(this) {
            header("Authorization", "Bearer $authToken")
            header("Client-Id", clientId)
            parameters.forEach { parameter ->
                parameter(parameter.first, parameter.second)
            }
        }
    }

    override suspend fun getGameByName(name: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>("name" to name)

        return payload.data.firstOrNull()
    }

    override suspend fun getGameById(id: String): GetGamesDataPayload? {
        val payload = "https://api.twitch.tv/helix/games".get<GetGamesPayload>("id" to id)

        return payload.data.firstOrNull()
    }

    override suspend fun getUser(userName: String): GetUsersDataPayload? {
        val payload = "https://api.twitch.tv/helix/users".get<GetUsersPayload>("login" to userName)

        return payload.data.firstOrNull()
    }

    override suspend fun getStreamByUserId(userId: String): StreamInfos? {
        val payload = "https://api.twitch.tv/helix/streams".get<StreamPayload>("user_id" to userId)

        return payload.data.firstOrNull()
    }
}