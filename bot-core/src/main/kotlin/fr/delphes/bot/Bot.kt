package fr.delphes.bot

import fr.delphes.configuration.BotConfiguration
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.discord.Discord
import kotlinx.coroutines.runBlocking

object Bot {
    fun build(
        configuration: BotConfiguration,
        publicUrl: String,
        configFilepath: String,
        vararg channelConfigurations: ChannelConfiguration
    ) {
        val bot = ClientBot(configuration, publicUrl, configFilepath)

        channelConfigurations.forEach { channelConfiguration ->
            bot.register(Channel(channelConfiguration, bot))
        }

        val discord = Discord()

        WebServer(bot, discord::endpoint)

        runBlocking {
            bot.resetWebhook()
        }

        // After initial state
        discord.connect()
    }
}