package fr.delphes.annotation.outgoingEvent.registerBuilder

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEventBuilder
import fr.delphes.annotation.serialization.SerializerModule
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder

val outgoingEventSerializerModule = SerializerModule(
    RegisterOutgoingEventBuilder::class,
    OutgoingEventBuilder::class,
)