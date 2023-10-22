package fr.delphes.bot.connector

import fr.delphes.bot.Bot
import fr.delphes.bot.event.outgoing.OutgoingEventRegistry
import kotlinx.serialization.modules.SerializersModule

abstract class ConnectorInitializer {
    abstract val serializerModule: SerializersModule
    abstract val outgoingEventRegistry: OutgoingEventRegistry

    abstract fun buildConnector(bot: Bot): Connector<*, *>
}