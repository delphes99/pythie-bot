package fr.delphes.annotation.incomingEvent

import com.squareup.kotlinpoet.ClassName
import fr.delphes.generation.PolymorphicSerializerModuleProcessorProvider

class RegisterIncomingEventSerializerModuleProcessorProvider : PolymorphicSerializerModuleProcessorProvider(
    RegisterIncomingEvent::class,
    ClassName("fr.delphes.bot.event.incoming", "IncomingEvent"),
    "IncomingEventSerializerModule",
)