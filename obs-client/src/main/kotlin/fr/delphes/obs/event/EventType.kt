package fr.delphes.obs.event

import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

enum class EventType(
    private val label: String,
    private val responseClazz: KClass<out Event>,
) {
    SWITCH_SCENE("SwitchScenes", SwitchScenes::class);

    @InternalSerializationApi
    companion object {
        fun findByLabel(label: String): EventType? {
            return values().firstOrNull { it.label == label }
        }

        fun deserialize(label: String, payload: String): Event? {
            return values()
                .firstOrNull { it.label == label }
                ?.responseClazz
                ?.serializer()
                ?.let { Serializer.decodeFromString(it, payload) }
        }
    }
}