package fr.delphes

import fr.delphes.connector.twitch.builder.SendMessageBuilder
import fr.delphes.features.FeatureConfiguration
import fr.delphes.features.twitch.NewTwitchCommand
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@InternalSerializationApi
fun main() {
    FeatureConfiguration.serializersModule
    val json = Json {
        serializersModule = FeatureConfiguration.serializersModule
    }

    val builder = listOf<fr.delphes.feature.featureNew.FeatureConfiguration>(
        NewTwitchCommand(
            "testTwitchFeature",
            TwitchChannel("delphes99"),
            "testconfiguration",
            listOf(
                SendMessageBuilder("message", "delphes99")
            )
        )
    )

    println(json.encodeToString(builder))
}