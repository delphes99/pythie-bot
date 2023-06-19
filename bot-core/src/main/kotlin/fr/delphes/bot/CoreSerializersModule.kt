package fr.delphes.bot

import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import statisticSerializerModule

val coreSerializersModule = SerializersModule {
    include(statisticSerializerModule)
    include(SerializersModule {
        polymorphic(IncomingEvent::class) {
            subclass(BotStarted::class)
        }
    })
}