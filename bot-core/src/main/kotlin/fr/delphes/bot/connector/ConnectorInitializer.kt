package fr.delphes.bot.connector

import fr.delphes.bot.Bot
import kotlinx.serialization.modules.SerializersModule

abstract class ConnectorInitializer {
    abstract val serializerModule: SerializersModule

    abstract fun buildConnector(bot: Bot): Connector<*, *>
}