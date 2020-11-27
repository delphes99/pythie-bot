package fr.delphes.twitch.pubsub

import fr.delphes.twitch.serialization.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

@Serializable
data class FrameMessagePayload(
    val topic: String,
    val message: String
) {
    val messageObject get() = Serializer.decodeFromString<MessagePayload>(message)
}