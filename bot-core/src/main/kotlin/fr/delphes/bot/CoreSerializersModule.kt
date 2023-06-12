package fr.delphes.bot

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.bot.event.outgoing.PlaySound
import fr.delphes.core.generated.coreFieldDescriptorSerializerModule
import fr.delphes.core.generated.coreIncomingEventSerializerModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import statisticSerializerModule

val coreSerializersModule = SerializersModule {
    polymorphic(OutgoingEventBuilder::class) {
        subclass(PlaySound.Companion.Builder::class)
    }
    include(statisticSerializerModule)
    /** Generated
     * @see annotation-generator module
     * */
    include(coreIncomingEventSerializerModule)
    include(coreFieldDescriptorSerializerModule)
}