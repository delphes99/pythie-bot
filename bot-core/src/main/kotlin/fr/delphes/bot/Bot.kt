package fr.delphes.bot

import fr.delphes.bot.connector.Connector
import fr.delphes.configuration.BotConfiguration
import kotlinx.coroutines.runBlocking

object Bot {
    fun build(
        configuration: BotConfiguration,
        publicUrl: String,
        configFilepath: String,
        connectors: List<Connector>
    ) {
        val bot = ClientBot(configuration, publicUrl, configFilepath, connectors)

        connectors.forEach { it.initChannel(bot) }

        WebServer(
            bot = bot,
            internalModules = connectors.map { connector -> { application -> connector.internalEndpoints(application, bot) } },
            publicModules = connectors.map { connector -> { application -> connector.publicEndpoints(application, bot) } }
        )

        // After initial state
        connectors.forEach { it.connect(bot) }

        //TODO move to connector
        runBlocking {
            bot.resetWebhook()
        }
    }
}