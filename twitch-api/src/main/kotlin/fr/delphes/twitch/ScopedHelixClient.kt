package fr.delphes.twitch

import fr.delphes.twitch.auth.AuthToken
import fr.delphes.utils.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class ScopedHelixClient(
    protected val clientId: String,
    @PublishedApi
    internal val getToken : suspend () -> AuthToken,
    @PublishedApi
    internal val getTokenRefreshed: suspend () -> AuthToken
) {
    @PublishedApi
    internal val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Serializer)
        }
    }

    protected suspend inline fun <reified T> String.get(
        vararg parameters: Pair<String, Any>
    ): T {
        return authorizeCall { token ->
            httpClient.get(this) {
                headersAndParameters(token, *parameters)
            }.body()
        }
    }

    protected suspend inline fun <reified T> String.post(
        payload: Any,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall { token ->
            httpClient.post(this) {
                headersAndParameters(token, *parameters)
                contentType(ContentType.Application.Json)
                setBody(payload)
            }.body()
        }
    }

    protected suspend inline fun <reified T> String.patch(
        payload: Any,
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall { token ->
            httpClient.patch(this) {
                headersAndParameters(token, *parameters)
                contentType(ContentType.Application.Json)
                setBody(payload)
            }.body()
        }
    }

    protected suspend inline fun <reified T> String.delete(
        vararg parameters: Pair<String, String>
    ): T {
        return authorizeCall { token ->
            httpClient.delete(this) {
                headersAndParameters(token, *parameters)
            }.body()
        }
    }

    @PublishedApi
    internal fun HttpRequestBuilder.headersAndParameters(
        authToken: AuthToken,
        vararg parameters: Pair<String, Any>
    ) {
        header("Authorization", "Bearer ${authToken.access_token}")
        header("Client-Id", clientId)
        parameters.forEach { parameter ->
            val serializedValue = when (parameter.second) {
                is LocalDateTime -> {
                    "${(parameter.second as LocalDateTime)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}Z" //TODO format twitch / Z?
                }
                else -> parameter.second
            }
            parameter(parameter.first, serializedValue)
        }
    }

    @PublishedApi
    internal suspend inline fun <reified T> authorizeCall(
        doCall: (AuthToken) -> T
    ): T {
        try {
            return doCall(getToken())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                return doCall(getTokenRefreshed())
            }
            throw e
        }
    }
}