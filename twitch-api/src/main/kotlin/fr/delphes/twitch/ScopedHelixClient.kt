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

abstract class ScopedHelixClient(
    protected val appCredential: TwitchAppCredential,
    @PublishedApi
    internal val scopedToken: WithAuthToken
) {
    @PublishedApi
    internal val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
    }

    protected suspend inline fun <reified T> String.get(
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(scopedToken) {
            httpClient.get(this) {
                headersAndParameters(scopedToken, *parameters)
            }
        }
    }

    protected suspend inline fun <reified T> String.post(
        payload: Any,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(scopedToken) {
            httpClient.post(this) {
                headersAndParameters(scopedToken, *parameters)
                contentType(ContentType.Application.Json)
                body = payload
            }
        }
    }

    protected suspend inline fun <reified T> String.patch(
        payload: Any,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(scopedToken) {
            httpClient.patch(this) {
                headersAndParameters(scopedToken, *parameters)
                contentType(ContentType.Application.Json)
                body = payload
            }
        }
    }

    protected suspend inline fun <reified T> String.delete(
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall(scopedToken) {
            httpClient.delete(this) {
                headersAndParameters(scopedToken, *parameters)
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