package fr.delphes.overlay

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.ConnectorInitializer
import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import fr.delphes.overlay.event.outgoing.PlaySound
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class OverlayInitializer : ConnectorInitializer() {
    override val incomingEventSerializerModule = SerializersModule { }
    override val outgoingEventBuilderSerializerModule = SerializersModule {
        polymorphic(LegacyOutgoingEventBuilder::class) {
            subclass(PlaySound.Companion.Builder::class)
        }
    }

    override fun buildConnector(bot: Bot) = OverlayConnector(bot)
}