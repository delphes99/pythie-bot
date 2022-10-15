package fr.delphes.obs.fromObs.event

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

enum class EventType(
    private val type: String,
    private val kClass: KClass<out Event>,
) {
    SWITCH_SCENE("CurrentProgramSceneChanged", CurrentProgramSceneChanged::class),
    SOURCE_FILTER_ENABLE_STATE_CHANGED("SourceFilterEnableStateChanged", SourceFilterEnableStateChanged::class);

    @OptIn(InternalSerializationApi::class)
    fun deserialize(payload: String, serializer: Json): Event {
        return serializer.decodeFromString(kClass.serializer(), payload)
    }

    @InternalSerializationApi
    companion object {
        fun findByType(type: String): EventType? {
            return values().firstOrNull { it.type == type }
        }
    }
}