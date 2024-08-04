package fr.delphes.bot.connector

import fr.delphes.bot.Bot
import fr.delphes.dynamicForm.DynamicFormRegistry
import kotlinx.serialization.modules.SerializersModule

abstract class ConnectorInitializer {
    abstract val serializerModule: SerializersModule
    abstract val dynamicFormRegistry: DynamicFormRegistry

    abstract fun buildConnector(bot: Bot): Connector<*, *>
}