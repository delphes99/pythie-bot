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
import fr.delphes.twitch.payload.pubsub.RewardRedeemed
import fr.delphes.twitch.serialization.Serializer
import fr.delphes.utils.exhaustive
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
    private val userCredential: TwitchUserCredential,
    private val userId: String,
    private val listenToReward: ((RewardRedemption) -> Unit)?
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
    }

    override suspend fun listen() {
        httpClient.wss(
            method = HttpMethod.Get,
            host = "pubsub-edge.twitch.tv",
        ) {
            if (listenToReward != null) {
                send(
                    Frame.Text(
                        Serializer.encodeToString(
                            ListenTo(
                                "LISTEN",
                                "community-points-channel-v1.$userId",
                                ListenToData(
                                    userCredential.authToken!!.access_token,
                                    "community-points-channel-v1.$userId"
                                )
                            )
                        )
                    )
                )
            }

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