package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
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

    val configFilepath = "A:\\pythiebot\\"
    val bot = Bot(
        tunnel.publicUrl,
        configFilepath,
        listOf(
            delphes99Features,
            delphestestFeatures
        ).flatten()
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
        )
    )
}