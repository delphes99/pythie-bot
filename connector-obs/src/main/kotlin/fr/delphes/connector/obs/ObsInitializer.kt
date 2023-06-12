package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.generated.generatedIncomingEventSerializerModule
import kotlinx.serialization.modules.SerializersModule

class ObsInitializer : ConnectorInitializer() {
    override val incomingEventSerializerModule = generatedIncomingEventSerializerModule
    override val outgoingEventBuilderSerializerModule = SerializersModule {}

    override fun buildConnector(bot: Bot) = ObsConnector(
        bot,
        bot.configuration,
    )
}