package fr.delphes.bot.event.incoming

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val incomingEventSerializerModule = SerializersModule {
    polymorphic(IncomingEvent::class) {
        subclass(BotStarted::class)
    }
}