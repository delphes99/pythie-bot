package fr.delphes.connector.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.discord.generated.discordPolymorphicSerializerModule

class DiscordInitializer : ConnectorInitializer() {
    override val serializerModule = discordPolymorphicSerializerModule

    override fun buildConnector(bot: Bot) = DiscordConnector(
        bot,
        bot.configuration,
    )
}