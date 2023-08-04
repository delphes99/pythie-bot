package fr.delphes.annotation.outgoingEvent.registerBuilder

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEventBuilder
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.generation.PolymorphicSerializerModuleProcessorProvider

class RegisterOutgoingEventBuilderSerializerModuleProcessorProvider : PolymorphicSerializerModuleProcessorProvider(
    RegisterOutgoingEventBuilder::class,
    OutgoingEventBuilder::class,
    "OutgoingEventBuilderSerializerModule",
)