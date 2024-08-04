package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.configuration.channel.delphes99.Delphes99Rewards
import fr.delphes.configuration.channel.delphes99.delphes99Channel
import fr.delphes.configuration.channel.delphes99.delphes99CustomFeatures
import fr.delphes.configuration.channel.delphes99.delphes99Features
import fr.delphes.configuration.channel.delphestest.delphestestChannel
import fr.delphes.configuration.channel.delphestest.delphestestCustomFeatures
import fr.delphes.configuration.loadProperties
import fr.delphes.connector.discord.DiscordInitializer
import fr.delphes.connector.obs.ObsInitializer
import fr.delphes.connector.twitch.TwitchInitializer
import fr.delphes.features.featureSerializersModule
import fr.delphes.features.generated.dynamicForm.featuresDynamicFormRegistry
import fr.delphes.overlay.OverlayInitializer
import kotlinx.serialization.InternalSerializationApi
import java.io.File

@InternalSerializationApi
fun main() {
    val props = loadProperties("configuration-pythiebot.properties")

    Ngrok.launch(props.getProperty("ngrok.path"))
    val tunnel = Ngrok.createHttpTunnel(80, props.getProperty("ngrok.tunnel.name"))

    val configFilepath = "A:${File.separator}pythiebot"

    val connectors = listOf(
        DiscordInitializer(),
        ObsInitializer(),
        TwitchInitializer(
            listOf(
                delphes99Channel,
                delphestestChannel
            ),
            Delphes99Rewards.configuredRewards
        ),
        OverlayInitializer(),
    )

    val configuration = BotConfiguration(
        configFilepath,
        tunnel.publicUrl,
    )

    Bot.launchBot(
        configuration,
        connectors,
        listOf(
            delphes99Features,
        ).flatten(),
        delphes99CustomFeatures + delphestestCustomFeatures,
        featuresDynamicFormRegistry,
        featureSerializersModule
    )
}