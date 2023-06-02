package fr.delphes.connector.obs.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val obsIncomingEventSerializerModule = SerializersModule {
    polymorphic(IncomingEvent::class) {
        subclass(SceneChanged::class)
        subclass(SourceFilterActivated::class)
        subclass(SourceFilterDeactivated::class)
    }
}