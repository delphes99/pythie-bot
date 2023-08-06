package fr.delphes.annotation.outgoingEvent

import fr.delphes.annotation.serialization.RegisterPolymorphic
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder

val outgoingEventRegisterPolymorphic = RegisterPolymorphic(
    RegisterOutgoingEventBuilder::class,
    OutgoingEventBuilder::class,
)