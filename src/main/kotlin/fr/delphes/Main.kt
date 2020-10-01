package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.bot.Ngrok
import fr.delphes.configuration.PropertiesBotConfiguration
import fr.delphes.configuration.channel.delphes99Channel
import fr.delphes.configuration.channel.delphestestChannel
import mu.KotlinLogging

val LOGGER = KotlinLogging.logger {}

fun main() {
    val tunnel = Ngrok.createHttpTunnel(80, "bot")
    LOGGER.debug { "Opened ngrok tunnel with public url : ${tunnel.publicUrl}" }

    Bot.build(
        PropertiesBotConfiguration(),
        tunnel.publicUrl,
        listOf(
            delphes99Channel,
            delphestestChannel
        )
    )
}