package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.configuration.BotConfiguration
import fr.delphes.configuration.ChannelConfiguration
import kotlinx.coroutines.runBlocking

object Bot {
    fun build(
        configuration: BotConfiguration,
        publicUrl: String,
        configFilepath: String,
        connectors: List<Connector>,
        vararg channelConfigurations: ChannelConfiguration
    ) {
        val bot = ClientBot(configuration, publicUrl, configFilepath, connectors)

        channelConfigurations.forEach { channelConfiguration ->
            bot.register(Channel(channelConfiguration, bot))
        }

        WebServer(bot, connectors.map { connector -> connector::endpoints })

        runBlocking {
            bot.resetWebhook()
        }

        // After initial state
        connectors.forEach(Connector::connect)
    }
}