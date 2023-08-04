package fr.delphes.annotation.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.generation.PolymorphicSerializerModuleProcessorProvider

class RegisterIncomingEventSerializerModuleProcessorProvider : PolymorphicSerializerModuleProcessorProvider(
    RegisterIncomingEvent::class,
    IncomingEvent::class,
    "IncomingEventSerializerModule",
)