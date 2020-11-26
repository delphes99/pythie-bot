package fr.delphes.bot

import fr.delphes.bot.util.http.httpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import java.util.concurrent.TimeUnit

class Ngrok {
    companion object {
        const val API_URL = "http://localhost:4040/api"
        private val LOGGER = KotlinLogging.logger {}

        fun launch(pathToNgrok: String) {
            var process: Process? = null
            try {
                LOGGER.info { "starting ngrok" }
                process = ProcessBuilder(pathToNgrok, "start", "--none")
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()
                if (process.waitFor(1, TimeUnit.SECONDS)) {
                    throw IllegalStateException("Ngrok early termination")
                }
                atShutdown {
                    process.waitFor(1, TimeUnit.SECONDS)
                    LOGGER.info { "destroying ngrok" }
                    process.destroy()
                }
            } catch (e: Exception) {
                val error = process?.errorStream?.bufferedReader()?.readText()?.let { "Error : $it" }
                val output = process?.inputStream?.bufferedReader()?.readText()?.let { "Output : $it" }
                val messages = listOfNotNull("Ngrok start failure : ${e.message}", error, output)
                LOGGER.error(e) { messages.joinToString("\n\n") }
                throw e
            }
        }

        fun createHttpTunnel(port: Int, name: String): Tunnel {
            val tunnel = Tunnel(port, name)
            LOGGER.debug { "Opened ngrok tunnel with public url : ${tunnel.publicUrl}" }
            atShutdown { runBlocking { tunnel.kill() } }
            return tunnel
        }

        private fun atShutdown(doStuff: () -> Unit) {
            Runtime.getRuntime().addShutdownHook(Thread { doStuff() })
        }
    }

    data class Tunnel(
        val port: Int,
        val name: String
    ) {
        val publicUrl: String

        init {
            publicUrl = runBlocking {
                kill()
                create()
            }
        }


        internal suspend fun kill() {
            try {
                LOGGER.debug { "Delete tunnels : $name" }
                httpClient.delete<HttpResponse>("$API_URL/tunnels/$name")
                httpClient.delete<HttpResponse>("$API_URL/tunnels/$name%20(http)")
            } catch (e: Exception) {
                LOGGER.error(e) { "Failed to delete tunnel : $name" }
            }
        }

        private suspend fun create(): String {
            LOGGER.debug { "Create tunnel : $name" }
            try {
                val response = httpClient.post<String> {
                    url("$API_URL/tunnels")
                    contentType(ContentType.Application.Json)
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