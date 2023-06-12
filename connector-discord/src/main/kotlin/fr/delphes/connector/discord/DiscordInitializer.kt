package fr.delphes.connector.discord

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.discord.generated.discordIncomingEventSerializerModule
import kotlinx.serialization.modules.SerializersModule

class DiscordInitializer : ConnectorInitializer() {
    override val incomingEventSerializerModule = discordIncomingEventSerializerModule
    override val outgoingEventBuilderSerializerModule = SerializersModule {}

    override fun buildConnector(bot: Bot) = DiscordConnector(
        bot,
        bot.configuration,
    )
}