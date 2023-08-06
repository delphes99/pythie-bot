package fr.delphes.annotation.incomingEvent

import fr.delphes.annotation.serialization.RegisterPolymorphic
import fr.delphes.bot.event.incoming.IncomingEvent

val incomingEventRegisterPolymorphic = RegisterPolymorphic(
    RegisterIncomingEvent::class,
    IncomingEvent::class,
)