package fr.delphes.twitch

import fr.delphes.twitch.auth.TwitchUserCredential
import fr.delphes.twitch.model.Feature
import fr.delphes.twitch.model.RewardRedemption
import fr.delphes.twitch.model.User
import fr.delphes.twitch.payload.ListenTo
import fr.delphes.twitch.payload.ListenToData
import fr.delphes.twitch.payload.pubsub.FrameMessage
import fr.delphes.twitch.payload.pubsub.FramePayload
import fr.delphes.twitch.payload.pubsub.FrameResponse
import fr.delphes.twitch.payload.pubsub.Ping
import fr.delphes.twitch.payload.pubsub.Pong
import fr.delphes.twitch.payload.pubsub.Reconnect
import fr.delphes.twitch.payload.pubsub.RewardRedeemed
import fr.delphes.twitch.serialization.Serializer
import fr.delphes.utils.exhaustive
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import java.time.Duration
import kotlin.coroutines.coroutineContext

@KtorExperimentalAPI
internal class PubSubClient(
    private val userCredential: TwitchUserCredential,
    private val userId: String,
    private val listenToReward: ((RewardRedemption) -> Unit)?
) : PubSubApi {
    private val pingInterval = Duration.ofMinutes(4)
    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
        }
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
        }
    }

    override suspend fun listen() {
        while (coroutineContext.isActive) {
            connect()
            LOGGER.info { "restart pubsub connection" }
        }
    }

    suspend fun connect() {
        httpClient.wss(
            method = HttpMethod.Get,
            host = "pubsub-edge.twitch.tv",
        ) {
            listenToTopics()

            ping()

            receiveFrames()
        }
    }

    private suspend fun DefaultClientWebSocketSession.ping() {
        launch {
            while (isActive) {
                delay(pingInterval.toMillis())
                send(Ping())
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.listenToTopics() {
        if (listenToReward != null) {
            send(
                ListenTo(
                    "LISTEN",
                    "community-points-channel-v1.$userId",
                    ListenToData(
                        userCredential.authToken!!.access_token,
                        "community-points-channel-v1.$userId"
                    )
                )
            )
        }
    }

    private suspend inline fun <reified T> DefaultClientWebSocketSession.send(item: T) {
        send(Frame.Text(Serializer.encodeToString(item)))
    }

    private suspend fun DefaultClientWebSocketSession.receiveFrames() {
        var reconnect: Reconnect
        LOGGER.info { "start receiving frames" }
        do {
            reconnect = try {
                incoming.receive().handle()
            } catch (e: ClosedReceiveChannelException) {
                Reconnect.RECONNECT
            } catch (e: Exception) {
                LOGGER.error(e) { "error receiving frame" }
                Reconnect.CONTINUE
            }
        } while (isActive && reconnect != Reconnect.RECONNECT)
    }

    private fun Frame.handle() : Reconnect {
        when (this) {
            is Frame.Text -> {
                val text = readText()
                try {
                    @Suppress("IMPLICIT_CAST_TO_ANY")
                    when (val framePayload = Serializer.decodeFromString<FramePayload>(text)) {
                        is FrameMessage -> {
                            when (val message = framePayload.data.messageObject) {
                                is RewardRedeemed -> {
                                    val redemption = message.data.redemption
                                    val reward = redemption.reward

                                    val rewardRedemption = RewardRedemption(
                                        Feature(
                                            reward.id,
                                            reward.title
                                        ),
                                        User(redemption.user.display_name),
                                        reward.cost
                                    )

                                    listenToReward?.invoke(rewardRedemption)
                                }
                            }.exhaustive()
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
                        Pong -> { /* NOTHING */ }
                        Reconnect -> return Reconnect.RECONNECT
                    }.exhaustive()
                } catch (e: Exception) {
                    LOGGER.error(e) { "Error while parsing frame : $text" }
                }
            }
            else -> {
                LOGGER.info { "unmanaged frame type ${frameType.name}" }
            }
        }
        return Reconnect.CONTINUE
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }

    enum class Reconnect {
        RECONNECT, CONTINUE
    }
}