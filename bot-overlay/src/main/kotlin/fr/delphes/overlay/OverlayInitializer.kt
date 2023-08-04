package fr.delphes.overlay

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.overlay.generated.overlayPolymorphicSerializerModule
import kotlinx.serialization.modules.SerializersModule

class OverlayInitializer : ConnectorInitializer() {
    override val serializerModule: SerializersModule = overlayPolymorphicSerializerModule

    override fun buildConnector(bot: Bot) = OverlayConnector(bot)
}