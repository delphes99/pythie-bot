package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.coreSerializersModule
import fr.delphes.configuration.channel.delphes99.delphes99Channel
import fr.delphes.configuration.channel.delphes99.delphes99CustomFeatures
import fr.delphes.configuration.channel.delphes99.delphes99Features
import fr.delphes.configuration.channel.delphestest.delphestestChannel
import fr.delphes.configuration.channel.delphestest.delphestestCustomFeatures
import fr.delphes.configuration.channel.delphestest.delphestestFeatures
import fr.delphes.configuration.loadProperties
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.discord.discordSerializerModule
import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.obs.obsSerializerModule
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.twitchSerializersModule
import fr.delphes.feature.FeatureConfigurationBuilderRegistry
import fr.delphes.feature.FeatureConfigurationType
import fr.delphes.features.featureSerializersModule
import fr.delphes.features.twitch.command.CustomCommandConfiguration
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
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
        //TODO centralize serialization module
        serializersModule = SerializersModule {
            include(coreSerializersModule)
            include(featureSerializersModule)
            include(discordSerializerModule)
            include(obsSerializerModule)
            include(twitchSerializersModule)
        }
    }

    val configuration = BotConfiguration(
        configFilepath,
        tunnel.publicUrl,
    )

    val bot = Bot(
        configuration,
        listOf(
            delphes99Features,
            delphestestFeatures
        ).flatten(),
        serializer,
        delphes99CustomFeatures + delphestestCustomFeatures,
        listOf(
            FeatureConfigurationBuilderRegistry(
                FeatureConfigurationType("TwitchCustomCommandConfiguration")
            ) { featureId ->
                CustomCommandConfiguration(id = featureId)
            },
        ),
    )

    //TODO connector configuration to regroup serializer + connector initialization
    bot.init(
        TwitchConnector(
            bot,
            bot.configuration,
            delphes99Channel,
            delphestestChannel
        ),
        DiscordConnector(
            bot,
            bot.configuration,
        ),
        ObsConnector(
            bot,
            bot.configuration,
        ),
    )
}