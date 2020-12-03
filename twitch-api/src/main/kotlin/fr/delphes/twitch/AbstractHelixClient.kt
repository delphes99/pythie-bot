package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchAppCredential
import fr.delphes.twitch.auth.WithAuthToken
import fr.delphes.utils.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

abstract class AbstractHelixClient(
    protected val appCredential: TwitchAppCredential
) {
    @PublishedApi
    internal val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
    }

    protected suspend inline fun <reified T> String.get(
        twitchUserCredential: WithAuthToken,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(twitchUserCredential) {
            httpClient.get(this) {
                headersAndParameters(twitchUserCredential, *parameters)
            }
        }
    }

    protected suspend inline fun <reified T> String.post(
        payload: Any,
        twitchUserCredential: WithAuthToken,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(twitchUserCredential) {
            httpClient.post(this) {
                headersAndParameters(twitchUserCredential, *parameters)
                contentType(ContentType.Application.Json)
                body = payload
            }
        }
    }

    protected suspend inline fun <reified T> String.patch(
        payload: Any,
        twitchUserCredential: WithAuthToken,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(twitchUserCredential) {
            httpClient.patch(this) {
                headersAndParameters(twitchUserCredential, *parameters)
                contentType(ContentType.Application.Json)
                body = payload
            }
        }
    }

    protected suspend inline fun <reified T> String.delete(
        twitchUserCredential: WithAuthToken,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(twitchUserCredential) {
            httpClient.delete(this) {
                headersAndParameters(twitchUserCredential, *parameters)
            }
        }
    }

    @PublishedApi
    internal fun HttpRequestBuilder.headersAndParameters(
        credentials: WithAuthToken,
        vararg parameters: Pair<String, String>
    ) {
        header("Authorization", "Bearer ${credentials.authToken!!.access_token}")
        header("Client-Id", appCredential.clientId)
        parameters.forEach { parameter ->
            parameter(parameter.first, parameter.second)
        }
    }

    @PublishedApi
    internal suspend inline fun <reified T> authorizeCall(
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
}