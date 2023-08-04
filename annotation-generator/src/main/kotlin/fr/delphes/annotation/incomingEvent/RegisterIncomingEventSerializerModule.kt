package fr.delphes.annotation.incomingEvent

import fr.delphes.annotation.serialization.SerializerModule
import fr.delphes.bot.event.incoming.IncomingEvent

val incomingEventSerializerModule = SerializerModule(
    RegisterIncomingEvent::class,
    IncomingEvent::class,
)