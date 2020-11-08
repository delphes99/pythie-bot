package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.configuration.PropertiesBotConfiguration
import fr.delphes.configuration.channel.delphes99Channel
import fr.delphes.configuration.channel.delphestestChannel
import fr.delphes.configuration.loadProperties

fun main() {
    val props = loadProperties("configuration-pythiebot.properties")

    Ngrok.launch(props.getProperty("ngrok.path"))
    val tunnel = Ngrok.createHttpTunnel(80, props.getProperty("ngrok.tunnel.name"))

    Bot.build(
        PropertiesBotConfiguration(),
        tunnel.publicUrl,
        "A:\\pythiebot\\configuration\\",
        delphes99Channel,
        delphestestChannel
    )
}