package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.obs.generated.obsOutgoingEventRegistry
import fr.delphes.obs.generated.obsPolymorphicSerializerModule

class ObsInitializer : ConnectorInitializer() {
    override val serializerModule = obsPolymorphicSerializerModule
    override val outgoingEventRegistry = obsOutgoingEventRegistry

    override fun buildConnector(bot: Bot) = ObsConnector(
        bot,
        bot.configuration,
    )
}