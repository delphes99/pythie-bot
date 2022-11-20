package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.BotFeatures
import fr.delphes.bot.Ngrok
import fr.delphes.configuration.channel.delphes99.delphes99Channel
import fr.delphes.configuration.channel.delphes99.delphes99CustomFeatures
import fr.delphes.configuration.channel.delphes99.delphes99Features
import fr.delphes.configuration.channel.delphestestChannel
import fr.delphes.configuration.channel.delphestestFeatures
import fr.delphes.configuration.loadProperties
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.builder.SendMessageBuilder
import fr.delphes.connector.twitch.builder.sendMessageMapper
import fr.delphes.descriptor.registry.DescriptorRegistry
import fr.delphes.descriptor.registry.MergeDescriptorRegistry
import fr.delphes.feature.FeatureConfigurationRepository
import fr.delphes.feature.FeaturesManager
import fr.delphes.features.FeatureSerializationConfiguration
import fr.delphes.features.twitch.command.EditableCommand
import fr.delphes.features.twitch.command.EditableCommandConfiguration
import fr.delphes.features.twitch.command.NewTwitchCommand
import fr.delphes.features.twitch.command.twitchCommandMapper
import fr.delphes.features.twitch.command.type
import fr.delphes.rework.feature.FeatureDefinition
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
        serializersModule = FeatureSerializationConfiguration.serializersModule
    }

    val featureConfigurationsPath = "${File.separator}feature${File.separator}features.json"

    val outgoingEventRegistry = DescriptorRegistry.of(
        sendMessageMapper
    )

    val bot = Bot(
        tunnel.publicUrl,
        configFilepath,
        listOf(
            delphes99Features,
            delphestestFeatures
        ).flatten(),
        listOf(
            EditableCommand(
                EditableCommandConfiguration(
                    "delphes99",
                    "!commandeTest",
                    0,
                    SendMessageBuilder("test !", "delphes99")
                )
            )
        ),
        //TODO remove when new features are implemented
        BotFeatures(
            mapOf(
                type to { id -> NewTwitchCommand(id) }),
            mapOf(
                SendMessageBuilder.type to SendMessageBuilder.description()
            )
        ),
        outgoingEventRegistry,
        serializer,
        buildFeatureManager(
            configFilepath,
            featureConfigurationsPath,
            serializer,
            outgoingEventRegistry,
            delphes99CustomFeatures
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
    configFilepath: String,
    featureConfigurationsPath: String,
    serializer: Json,
    outgoingEventRegistry: DescriptorRegistry,
    customFeatures: List<FeatureDefinition>
): FeaturesManager {
    val featureRepository = FeatureConfigurationRepository(
        "${configFilepath}$featureConfigurationsPath",
        serializer
    )
    val featureRegistry = DescriptorRegistry.of(twitchCommandMapper)
    val globalDescriptorRegistry = MergeDescriptorRegistry(featureRegistry, outgoingEventRegistry)

    return FeaturesManager(featureRepository, featureRegistry, globalDescriptorRegistry, customFeatures)
}