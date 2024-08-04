package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.obs.generated.dynamicForm.obsDynamicFormRegistry
import fr.delphes.obs.generated.obsPolymorphicSerializerModule

class ObsInitializer : ConnectorInitializer() {
    override val serializerModule = obsPolymorphicSerializerModule
    override val dynamicFormRegistry = obsDynamicFormRegistry

    override fun buildConnector(bot: Bot) = ObsConnector(
        bot,
        bot.configuration,
    )
}