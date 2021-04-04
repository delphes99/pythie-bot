package fr.delphes

import fr.delphes.connector.twitch.builder.SendMessageBuilder
import fr.delphes.features.FeatureConfiguration
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@InternalSerializationApi
fun main() {
    FeatureConfiguration.serializersModule
    val json = Json {
        serializersModule = FeatureConfiguration.serializersModule
    }

    val builder = SendMessageBuilder("coucou %user%", "delphes99")

    println(json.encodeToString(builder))
}