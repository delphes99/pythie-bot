package fr.delphes.annotation

import com.squareup.kotlinpoet.ClassName
import fr.delphes.generation.PolymorphicSerializerModuleProcessorProvider

class RegisterIncomingEventSerializerModuleProcessorProvider : PolymorphicSerializerModuleProcessorProvider(
    RegisterIncomingEvent::class.java,
    ClassName("fr.delphes.bot.event.incoming", "IncomingEvent"),
    "generatedIncomingEventSerializerModule",
)