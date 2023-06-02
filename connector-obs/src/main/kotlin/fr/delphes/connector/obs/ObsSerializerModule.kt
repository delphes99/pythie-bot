package fr.delphes.connector.obs

import fr.delphes.connector.obs.incomingEvent.obsIncomingEventSerializerModule
import kotlinx.serialization.modules.SerializersModule

val obsSerializerModule = SerializersModule {
    include(obsIncomingEventSerializerModule)
}