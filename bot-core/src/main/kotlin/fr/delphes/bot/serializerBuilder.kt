package fr.delphes.bot

import fr.delphes.bot.connector.ConnectorInitializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

fun buildSerializer(
    featureSerializersModule: SerializersModule, connectors: List<ConnectorInitializer>,
) = Json {
    ignoreUnknownKeys = true
    isLenient = false
    encodeDefaults = true
    coerceInputValues = true
    serializersModule = SerializersModule {
        include(coreSerializersModule)
        include(featureSerializersModule)
        connectors.forEach {
            include(it.serializerModule)
        }
    }
}

fun buildSerializer(
    vararg connectors: ConnectorInitializer,
) = buildSerializer(SerializersModule {}, connectors.toList())