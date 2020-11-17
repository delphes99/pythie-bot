package fr.delphes.twitch

import fr.delphes.twitch.payload.ListenTo
import fr.delphes.twitch.payload.ListenToData
import fr.delphes.twitch.payload.pubsub.FrameMessage
import fr.delphes.twitch.payload.pubsub.FramePayload
import fr.delphes.twitch.payload.pubsub.FrameResponse
import fr.delphes.twitch.serialization.Serializer
import fr.delphes.twitch.util.exhaustive
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import mu.KotlinLogging

@KtorExperimentalAPI
internal class PubSubClient(
    private val authToken: String,
    private val userId: String
) : PubSubApi {
    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 5000000
            requestTimeoutMillis = 5000000
        }
    }

    override suspend fun listenToChannelReward() {
        httpClient.wss(
            method = HttpMethod.Get,
            host = "pubsub-edge.twitch.tv"
        ) {
            send(
                Frame.Text(
                    Serializer.encodeToString(
                        ListenTo(
                            "LISTEN",
                            "community-points-channel-v1.$userId",
                            ListenToData(
                                authToken,
                                "community-points-channel-v1.$userId"
                            )
                        )
                    )
                )
            )

            while (isActive) {
                try {
                    val frame = incoming.receive()
                    parseFrame(frame)
                } catch (e: Exception) {
                    LOGGER.error(e) { "error receiving frame" }
                }
            }
        }
    }

    private fun parseFrame(frame: Frame) {
        when (frame) {
            is Frame.Text -> {
                val text = frame.readText()
                try {
                    @Suppress("IMPLICIT_CAST_TO_ANY")
                    when (val framePayload = Serializer.decodeFromString<FramePayload>(text)) {
                        is FrameMessage -> {
                            //TODO
                            println(framePayload.data.messageObject)
                        }
                        is FrameResponse -> {
                            if (framePayload.error.isBlank()) {
                                LOGGER.info {
                                    "Listen to : ${framePayload.nonce}"
                                }
                            } else {
                                LOGGER.error { "Error listening to : ${framePayload.nonce}" }
                            }
                        }
                    }.exhaustive()
                } catch (e: Exception) {
                    LOGGER.error(e) { "Error while parsing frame : $text" }
                }
            }
            else -> {
                LOGGER.info { "unmanaged frame type ${frame.frameType.name}" }
            }
        }
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}