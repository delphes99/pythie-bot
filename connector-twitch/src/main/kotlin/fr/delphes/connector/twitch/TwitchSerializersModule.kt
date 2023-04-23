package fr.delphes.connector.twitch

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val twitchSerializersModule = SerializersModule {
    polymorphic(OutgoingEventBuilder::class) {
        subclass(SendMessage.Builder::class)
    }
}