package fr.delphes.connector.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.discord.generated.discordPolymorphicSerializerModule
import fr.delphes.discord.generated.dynamicForm.discordDynamicFormRegistry

class DiscordInitializer : ConnectorInitializer() {
    override val serializerModule = discordPolymorphicSerializerModule
    override val dynamicFormRegistry = discordDynamicFormRegistry

    override fun buildConnector(bot: Bot) = DiscordConnector(
        bot,
        bot.configuration,
    )
}