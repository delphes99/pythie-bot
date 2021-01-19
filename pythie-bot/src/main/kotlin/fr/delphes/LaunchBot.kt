package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.configuration.PropertiesBotConfiguration
import fr.delphes.configuration.channel.delphes99.delphes99Channel
import fr.delphes.configuration.loadProperties
import fr.delphes.connector.discord.DiscordConnector
import fr.delphes.connector.discord.DiscordState

fun main() {
    val props = loadProperties("configuration-pythiebot.properties")

    Ngrok.launch(props.getProperty("ngrok.path"))
    val tunnel = Ngrok.createHttpTunnel(80, props.getProperty("ngrok.tunnel.name"))

    val configuration = PropertiesBotConfiguration()

    Bot.build(
        configuration,
        tunnel.publicUrl,
        "A:\\pythiebot\\",
        listOf(
            TwitchConnector(delphes99Channel),
            DiscordConnector(DiscordState.Configured(configuration.discordOAuth))
        ),
        delphes99Channel
        //,delphestestChannel
    )
}