package fr.delphes.bot

import fr.delphes.configuration.BotConfiguration
import fr.delphes.configuration.ChannelConfiguration

object Bot {
    fun build(
        configuration: BotConfiguration,
        publicUrl: String,
        channelConfigurations: List<ChannelConfiguration>
    ) {
        val bot = ClientBot(configuration, publicUrl)

        channelConfigurations.forEach { channelConfiguration ->
            bot.register(Channel(channelConfiguration, bot))
        }

        WebServer(bot)

        bot.subscribeWebhooks()
    }
}