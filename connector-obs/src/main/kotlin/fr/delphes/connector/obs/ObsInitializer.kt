package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.obs.generated.dynamicForm.obsDynamicFormRegistry
import fr.delphes.obs.generated.obsPolymorphicSerializerModule
import fr.delphes.obs.generated.outgoingEvent.obsOutgoingEventRegistry

class ObsInitializer : ConnectorInitializer() {
    override val serializerModule = obsPolymorphicSerializerModule
    override val outgoingEventRegistry = obsOutgoingEventRegistry
    override val dynamicFormRegistry = obsDynamicFormRegistry

    override fun buildConnector(bot: Bot) = ObsConnector(
        bot,
        bot.configuration,
    )
}