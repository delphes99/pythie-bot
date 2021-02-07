package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.configuration.PropertiesBotConfiguration
import fr.delphes.configuration.channel.delphes99.delphes99Channel
import fr.delphes.configuration.channel.delphes99.delphes99Features
import fr.delphes.configuration.channel.delphestestChannel
import fr.delphes.configuration.channel.delphestestFeatures
import fr.delphes.configuration.loadProperties
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.twitch.TwitchConnector

fun main() {
    val props = loadProperties("configuration-pythiebot.properties")

    Ngrok.launch(props.getProperty("ngrok.path"))
    val tunnel = Ngrok.createHttpTunnel(80, props.getProperty("ngrok.tunnel.name"))

    val configuration = PropertiesBotConfiguration()

    val configFilepath = "A:\\pythiebot\\"
    Bot(
        configuration,
        tunnel.publicUrl,
        configFilepath,
        listOf(
            TwitchConnector(configFilepath, delphes99Channel, delphestestChannel),
            DiscordConnector(configFilepath)
        ),
        listOf(
            delphes99Features,
            delphestestFeatures
        ).flatten()
    ).init()
}