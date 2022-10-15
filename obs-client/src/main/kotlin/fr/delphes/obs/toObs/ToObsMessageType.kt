package fr.delphes.obs.toObs

import fr.delphes.obs.message.Message
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.reflect.KClass

enum class ToObsMessageType(
    @PublishedApi
    internal val code: Int,
    @PublishedApi
    internal val kClass: KClass<out ToObsMessagePayload>
) {
    IDENTIFY(1, IdentifyPayload::class),
    REQUEST(6, RequestPayload::class);

    companion object {
        inline fun <reified T: ToObsMessagePayload> buildMessageFrom(payload: T, serializer: Json): Message {
            val payloadJson = serializer.encodeToJsonElement(payload)
            return values().firstOrNull { it.kClass == T::class }?.code?.let {
                Message(it, payloadJson)
            } ?: throw IllegalArgumentException("Unknown payload type ${payload::class}")
        }
    }
}