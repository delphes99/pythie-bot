package fr.delphes.twitch.pubsub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FramePayload

@Serializable
@SerialName("MESSAGE")
data class FrameMessage(
    val data: FrameMessagePayload
): FramePayload()

@Serializable
@SerialName("RESPONSE")
data class FrameResponse(
    val error: String,
    val nonce: String
): FramePayload()

@Serializable
@SerialName("PONG")
object Pong : FramePayload()

@Serializable
@SerialName("RECONNECT")
object Reconnect : FramePayload()