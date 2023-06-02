package fr.delphes.connector.discord.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val discordIncomingEventSerializerModule = SerializersModule {
    polymorphic(IncomingEvent::class) {
        subclass(NewGuildMember::class)
    }
}