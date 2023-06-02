package fr.delphes.bot.connector

import fr.delphes.bot.Bot
import kotlinx.serialization.modules.SerializersModule

abstract class ConnectorInitializer {
    abstract val incomingEventSerializerModule: SerializersModule
    abstract val outgoingEventBuilderSerializerModule: SerializersModule

    abstract fun buildConnector(bot: Bot): Connector<*, *>
}