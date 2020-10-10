package fr.delphes.bot

import fr.delphes.bot.storage.serialization.Serializer
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import mu.KotlinLogging

class Ngrok {
    companion object {
        const val API_URL = "http://localhost:4040/api"
        private val LOGGER = KotlinLogging.logger {}

        fun createHttpTunnel(port: Int, name: String): Tunnel {
            val tunnel = Tunnel(port, name)
            Runtime.getRuntime().addShutdownHook(Thread { runBlocking { tunnel.kill(HttpClient()) } })
            return tunnel
        }
    }

    data class Tunnel(
        val port: Int,
        val name: String
    ) {
        val publicUrl: String

        init {
            publicUrl = runBlocking {
                val httpClient = HttpClient() {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(Serializer)
                    }
                }
                kill(httpClient)
                create(httpClient)
            }
        }


        internal suspend fun kill(httpClient: HttpClient) {
            try {
                LOGGER.debug { "Delete tunnels : $name" }
                httpClient.delete<HttpResponse>("$API_URL/tunnels/$name")
                httpClient.delete<HttpResponse>("$API_URL/tunnels/$name%20(http)")
            } catch (e: Exception) {
                LOGGER.error(e) { "Failed to delete tunnel : $name" }
            }
        }

        private suspend fun create(httpClient: HttpClient): String {
            LOGGER.debug { "Create tunnel : $name" }
            try {
                val response = httpClient.post<String> {
                    url("$API_URL/tunnels")
                    contentType(ContentType.parse("application/json"))
                    body = CreateTunnelBody(port.toString(), "http", name)
                }
                return EXTRACT_PUBLIC_URL.find(response)!!.groups[1]!!.value
            } catch (e: Exception) {
                LOGGER.debug { "Failed creation of tunnel : $name" }
                throw e
            }
        }

        companion object {
            private val EXTRACT_PUBLIC_URL = Regex(".*\"public_url\":\"(https://.*\\.ngrok\\.io)\".*")
        }
    }

    @Serializable
    private data class CreateTunnelBody(
        val addr: String,
        val proto: String,
        val name: String
    )
}