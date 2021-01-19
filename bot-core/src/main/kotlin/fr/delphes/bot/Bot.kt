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

        WebServer(
            bot = bot,
            internalModules = connectors.map { connector -> connector::internalEndpoints },
            publicModules = connectors.map { connector -> connector::publicEndpoints }
        )

        // After initial state
        connectors.forEach { it.connect(bot) }

        //TODO move to connector
        runBlocking {
            bot.resetWebhook()
        }
    }
}