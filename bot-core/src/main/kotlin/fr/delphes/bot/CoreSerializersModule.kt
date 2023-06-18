package fr.delphes.bot

import fr.delphes.core.generated.coreFieldDescriptorSerializerModule
import fr.delphes.core.generated.coreIncomingEventSerializerModule
import kotlinx.serialization.modules.SerializersModule
import statisticSerializerModule

val coreSerializersModule = SerializersModule {
    include(statisticSerializerModule)
    /** Generated
     * @see annotation-generator module
     * */
    include(coreIncomingEventSerializerModule)
    include(coreFieldDescriptorSerializerModule)
}