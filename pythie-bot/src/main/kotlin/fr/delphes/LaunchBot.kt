package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.configuration.channel.delphes99.delphes99Channel
import fr.delphes.configuration.channel.delphes99.delphes99CustomFeatures
import fr.delphes.configuration.channel.delphes99.delphes99Features
import fr.delphes.configuration.channel.delphestest.delphestestChannel
import fr.delphes.configuration.channel.delphestest.delphestestCustomFeatures
import fr.delphes.configuration.channel.delphestest.delphestestFeatures
import fr.delphes.configuration.loadProperties
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.feature.FeaturesManager
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.state.StateManager
import fr.delphes.state.state.ClockState
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import java.io.File

@InternalSerializationApi
fun main() {
    val props = loadProperties("configuration-pythiebot.properties")

    Ngrok.launch(props.getProperty("ngrok.path"))
    val tunnel = Ngrok.createHttpTunnel(80, props.getProperty("ngrok.tunnel.name"))

    val configFilepath = "A:${File.separator}pythiebot"

    val serializer = Json {
        ignoreUnknownKeys = true
        isLenient = false
        encodeDefaults = true
        coerceInputValues = true
    }

    val bot = Bot(
        tunnel.publicUrl,
        configFilepath,
        listOf(
            delphes99Features,
            delphestestFeatures
        ).flatten(),
        serializer,
        buildFeatureManager(
            delphes99CustomFeatures + delphestestCustomFeatures
        )
    )

    bot.init(
        TwitchConnector(
            bot,
            configFilepath,
            delphes99Channel,
            delphestestChannel
        ),
        DiscordConnector(
            bot,
            configFilepath
        ),
        ObsConnector(
            bot,
            configFilepath
        ),
    )
}

private fun buildFeatureManager(
    customFeatures: List<FeatureDefinition>
): FeaturesManager {
    val stateManager = StateManager()
        .withState(ClockState())

    return FeaturesManager(stateManager, customFeatures)
}